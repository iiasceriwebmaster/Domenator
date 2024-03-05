package md.webmasterstudio.domenator.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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

        notificationDate.text = notification?.date
        notificationTitle.text = notification?.title
        notificationPreview.text = notification?.content

        // Set click listener on entire item view
        mConvertView.setOnClickListener {
            notification?.isExpanded = !notification?.isExpanded!!

            // Update visibility based on isExpanded state
            tapToSeeMore.visibility =
                if (notification?.isExpanded == false) View.VISIBLE else View.GONE
            notificationPreview.maxLines = if (notification?.isExpanded == false) 1 else 999
            notificationPreview.ellipsize =
                if (notification?.isExpanded == false) android.text.TextUtils.TruncateAt.END else null
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

