package md.webmasterstudio.domenator.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.databinding.ActivityReportBinding
import md.webmasterstudio.domenator.ui.adapters.NotificationAdapter
import md.webmasterstudio.domenator.ui.adapters.NotificationItem
import md.webmasterstudio.domenator.ui.adapters.ReportAdapter
import md.webmasterstudio.domenator.ui.adapters.ReportItem

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.activityReportMainLayout)
        ViewCompat.setOnApplyWindowInsetsListener(binding.activityReportMainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Create dummy list of notifications
        val reports = mutableListOf<ReportItem>()
        for (i in 1..10) {
            val date = "2024-03-$i" // Example date
            val report = ReportItem(date, "002569 km", "150 L | 26.80$/L")
            reports.add(report)
        }

        // Create adapter and set it to ListView or RecyclerView
        val adapter = ReportAdapter(reports)
        binding.reportRecyclerView.adapter = adapter
    }
}