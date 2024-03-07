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
import md.webmasterstudio.domenator.ui.fragments.AddReportDialogFragment

class ReportActivity : AppCompatActivity(),
    AddReportDialogFragment.DialogAddReportFragmentListener {

    private lateinit var binding: ActivityReportBinding
    lateinit var reports: MutableList<ReportItem>

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
            showDialog()
        }

        reports = mutableListOf<ReportItem>()
//        for (i in 10 downTo 1) {
//            val date = "2024-03-$i" // Example date
//            val report = ReportItem(date, "002569 km", "150 L | 26.80 $/L")
//            reports.add(report)
//        }

        updateListUI()


        binding.rightButton.setOnClickListener {
            startActivity(Intent(this, NotificationActivity::class.java))
        }

        binding.leftButton.setOnClickListener {
            binding.activityReportMainLayout.open()
        }


        binding.finishReportBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    override fun onReportInfoAdded(reportItem: ReportItem) {
        reports.add(reportItem)
        updateListUI()
    }

    private fun updateListUI() {
        if (reports.isNotEmpty()) {
            binding.emptyTextLL.visibility = View.GONE
            binding.reportRecyclerView.visibility = View.VISIBLE
        } else {
            binding.emptyTextLL.visibility = View.VISIBLE
            binding.reportRecyclerView.visibility = View.GONE
        }

        val adapter = ReportAdapter(reports) {
            val reportItem = reports[it]
            showDialog(reportItem)
        }
        binding.reportRecyclerView.adapter = adapter
    }

    private fun showDialog(reportItem: ReportItem? = null) {
        val dialog = AddReportDialogFragment()

        if (reportItem != null) {
            val bundle = Bundle()
            bundle.putString("km", reportItem.km)
            bundle.putString("date", reportItem.date)
            bundle.putString("pricePerUnit", reportItem.pricePerUnit)
            bundle.putString("quantity", reportItem.quantity)
            dialog.arguments = bundle
        }
        dialog.show(supportFragmentManager, "AddReportDialogFragment")
    }
}