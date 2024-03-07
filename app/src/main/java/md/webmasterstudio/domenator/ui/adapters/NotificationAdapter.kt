package md.webmasterstudio.domenator.ui.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import md.webmasterstudio.domenator.R

class NotificationItem(val date: String, val title: String, val content: String) {
    var isExpanded: Boolean = false
}

class NotificationAdapter(context: Context, notificationItems: List<NotificationItem>) :
    ArrayAdapter<NotificationItem>(context, 0, notificationItems) {

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
            Log.d("NotificationAdapter", "updateUI called for position $position with isExpanded: ${notification.isExpanded}")
            if (notification.isExpanded) {

                notificationPreview.maxLines = Int.MAX_VALUE
                notificationPreview.ellipsize = null
            } else {

                notificationPreview.maxLines = 1
                notificationPreview.ellipsize = android.text.TextUtils.TruncateAt.END
            }
            mConvertView.post {
                tapToSeeMore.visibility = if (notification?.isExpanded == true) View.GONE else View.VISIBLE
            }
        }

        // Set click listener on entire item view
        mConvertView.setOnClickListener {
            Log.d("NotificationAdapter", "Click listener called for position $position")
            notificationStatusIV.visibility = View.GONE
            notification?.isExpanded = notification?.isExpanded?.xor(true) == true
            Log.d("NotificationAdapter", "isExpanded set to: ${notification?.isExpanded}")


            if (notification != null) {
                Log.d("NotificationAdapter", "updateUI called for position $position with isExpanded: ${notification.isExpanded}")
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
            Log.d("NotificationAdapter", "After updateUI, isExpanded is now: ${notification?.isExpanded}")
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

