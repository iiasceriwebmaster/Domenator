package md.webmasterstudio.domenator.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import md.webmasterstudio.domenator.R
class NotificationItem(val date: String, val title: String, val preview: String)

class NotificationAdapter(context: Context, notificationItems: List<NotificationItem>) :
    ArrayAdapter<NotificationItem>(context, 0, notificationItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var mConvertView = convertView
        val notification = getItem(position)

        if (mConvertView == null) {
            mConvertView =
                LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        }

        val notificationDate: TextView = mConvertView!!.findViewById(R.id.notificationDate)
        val notificationTitle: TextView = mConvertView.findViewById(R.id.notificationTitle)
        val notificationPreview: TextView = mConvertView.findViewById(R.id.notificationPreview)
        val viewMoreButton: Button = mConvertView.findViewById(R.id.viewMoreButton)

        notificationDate.text = notification?.date
        notificationTitle.text = notification?.title
        notificationPreview.text = notification?.preview

        // Set onClick listener for "View More" button
        viewMoreButton.setOnClickListener {
            // Handle click action here
        }

        return mConvertView
    }
}

