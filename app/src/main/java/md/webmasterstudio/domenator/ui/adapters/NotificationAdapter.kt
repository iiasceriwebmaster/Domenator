package md.webmasterstudio.domenator.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.entity.NotificationEntity

class NotificationAdapter(context: Context, notificationEntityItems: List<NotificationEntity>) :
    ArrayAdapter<NotificationEntity>(context, 0, notificationEntityItems) {

    companion object {
        const val TEXT_THRESHOLD_CHARACTERS = 45
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        val notification = getItem(position)

        if (mConvertView == null) {
            mConvertView =
                LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        }

        val notificationDate: TextView = mConvertView!!.findViewById(R.id.notificationDate)
        val notificationTitle: TextView = mConvertView.findViewById(R.id.notificationTitle)
        val notificationPreview: TextView = mConvertView.findViewById(R.id.notificationContent)
        val tapToSeeMore: TextView = mConvertView.findViewById(R.id.tapToSeeMore)
        val notificationStatusIV: ImageView = mConvertView.findViewById(R.id.notificationStatusIV)

        notificationDate.text = notification?.date
        notificationTitle.text = notification?.title
        notificationPreview.text = notification?.content

        if (notification != null) {
            mConvertView.post {
                tapToSeeMore.visibility = if (notification.isExpanded) View.GONE else View.VISIBLE
            }
            if (notification.isExpanded) {

                notificationPreview.maxLines = Int.MAX_VALUE
                notificationPreview.ellipsize = null
            } else {

                notificationPreview.maxLines = 1
                notificationPreview.ellipsize = android.text.TextUtils.TruncateAt.END
            }
        }

        // Set click listener on entire item view
        mConvertView.setOnClickListener {
            notificationStatusIV.visibility = View.GONE
            notification?.isExpanded = notification?.isExpanded?.xor(true) == true


            if (notification != null) {
                if (notification.isExpanded) {

                    notificationPreview.maxLines = Int.MAX_VALUE
                    notificationPreview.ellipsize = null
                } else { tapToSeeMore.visibility = View.VISIBLE
                    notificationPreview.maxLines = 1
                    notificationPreview.ellipsize = android.text.TextUtils.TruncateAt.END
                }
            }

            mConvertView.post {
                tapToSeeMore.visibility = if (notification?.isExpanded == true) View.GONE else View.VISIBLE
            }
        }

        // Update visibility based on isExpanded state
        if (notificationPreview.text.length > TEXT_THRESHOLD_CHARACTERS) {
            tapToSeeMore.visibility = View.VISIBLE
        } else {
            tapToSeeMore.visibility = View.GONE
        }

        return mConvertView
    }

}

