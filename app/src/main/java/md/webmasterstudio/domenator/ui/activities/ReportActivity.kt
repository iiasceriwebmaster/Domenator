package md.webmasterstudio.domenator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.databinding.ActivityReportBinding
import md.webmasterstudio.domenator.ui.adapters.ReportAdapter
import md.webmasterstudio.domenator.ui.adapters.ReportItem
import md.webmasterstudio.domenator.ui.fragments.AddCarInfoDialogFragment

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

        val date = intent.getStringExtra("date")
        val km = intent.getStringExtra("km") + " km"
        val licencePlateNr = intent.getStringExtra("licencePlateNr")

        binding.licencePlateNrTV.text = licencePlateNr
        binding.titleKm.text = km
        binding.titleDate.text = date

        binding.standardFab.setOnClickListener {
            val dialog = AddCarInfoDialogFragment()
            dialog.show(supportFragmentManager, "AddCarInfoDialogFragment")
        }

        val reports = mutableListOf<ReportItem>()
//        for (i in 10 downTo 1) {
//            val date = "2024-03-$i" // Example date
//            val report = ReportItem(date, "002569 km", "150 L | 26.80 $/L")
//            reports.add(report)
//        }

        // Create adapter and set it to ListView or RecyclerView

        if (reports.isNotEmpty()) {
            binding.emptyTextLL.visibility = View.GONE
            binding.reportRecyclerView.visibility = View.VISIBLE
        }

        val adapter = ReportAdapter(reports)
        binding.reportRecyclerView.adapter = adapter

        binding.rightButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        binding.leftButton.setOnClickListener {
            binding.activityReportMainLayout.open()
        }
        binding.navView.menu.getItem(0).isChecked = false
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.activityReportMainLayout.close()
            when (menuItem.itemId) {
                R.id.nav_user_profile -> {
                    // Open user profile
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    return@setNavigationItemSelectedListener true
                }

                R.id.nav_car_acceptance -> {
                    // Open car acceptance activity
                    startActivity(Intent(this, CarReceptionActivity::class.java))
                    return@setNavigationItemSelectedListener true
                }

                R.id.nav_driver_regulations -> {
                    // Open driver regulations activity
                    val intent = Intent(this, TextualActivity::class.java)
                    intent.putExtra("txt", "driver regulations")
                    startActivity(intent)
                    return@setNavigationItemSelectedListener true
                }

                R.id.nav_driver_help -> {
                    // Open driver help activity
                    val intent = Intent(this, TextualActivity::class.java)
                    intent.putExtra("txt", "driver help")
                    startActivity(intent)
                    return@setNavigationItemSelectedListener true
                }

                R.id.nav_logout -> {
                    // Perform logout action
//                logoutUser()
                    return@setNavigationItemSelectedListener true
                }

                else -> return@setNavigationItemSelectedListener super.onOptionsItemSelected(
                    menuItem
                )
            }
        }
    }
}