package md.webmasterstudio.domenator.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.data.db.DomenatorDatabase
import md.webmasterstudio.domenator.data.db.entity.NotificationEntity
import md.webmasterstudio.domenator.databinding.ActivityNotificationBinding
import md.webmasterstudio.domenator.ui.adapters.NotificationAdapter
import md.webmasterstudio.domenator.ui.viewmodels.NotificationViewModel

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var appDatabase: DomenatorDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appDatabase = DomenatorDatabase.getInstance(applicationContext)
        notificationViewModel = NotificationViewModel(appDatabase.notificationsDao())

        // Create dummy list of notifications
//        dummyList()

        notificationViewModel.allNotifications.observe(this) { notifications ->
            // Update the adapter with the observed list of notifications
            val adapter = NotificationAdapter(this, notifications)
            binding.notificationListView.adapter = adapter
        }

        binding.leftButton.setOnClickListener {
            onBackPressed()
        }
    }
    fun dummyList() {
        for (i in 1..10) {
            val date = "2024-03-$i" // Example date
            val title = "NotificationEntity $i" // Example title
            val preview =
                "Preview of NotificationEntity $i With this modification, \n" +
                        "clicking on any item within the list will toggle the isExpanded state of the corresponding\n" +
                        "NotificationItem, and notifyDataSetChanged() will " // Example preview
            val notificationEntity = NotificationEntity(date=date, title=title, content = preview)
            notificationViewModel.insert(notificationEntity)
        }
    }

}