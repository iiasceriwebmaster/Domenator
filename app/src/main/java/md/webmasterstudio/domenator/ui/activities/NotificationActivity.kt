package md.webmasterstudio.domenator.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.databinding.ActivityNotificationBinding
import md.webmasterstudio.domenator.ui.adapters.NotificationAdapter
import md.webmasterstudio.domenator.ui.adapters.NotificationItem

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()

    }

    private fun setupViews() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Create dummy list of notifications
        val notifications = mutableListOf<NotificationItem>()
        for (i in 1..10) {
            val date = "2024-03-$i" // Example date
            val title = "Notification $i" // Example title
            val preview = "Preview of Notification $i" // Example preview
            val notification = NotificationItem(date, title, preview)
            notifications.add(notification)
        }

        // Create adapter and set it to ListView or RecyclerView
        val adapter = NotificationAdapter(this, notifications)
        binding.notificationListView.adapter = adapter

        // Handle click on notification items (optional)
        binding.notificationListView.setOnItemClickListener { _, _, position, _ ->
            // Handle click action on notification item here
        }
    }
}