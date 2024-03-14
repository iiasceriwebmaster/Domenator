package md.webmasterstudio.domenator.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEachIndexed
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.entity.ReportInfoEntity
import md.webmasterstudio.domenator.databinding.ActivityReportBinding
import md.webmasterstudio.domenator.ui.activities.login.LoginActivity
import md.webmasterstudio.domenator.ui.adapters.ReportAdapter
import md.webmasterstudio.domenator.ui.fragments.AddReportDialogFragment
import md.webmasterstudio.domenator.ui.viewmodels.ReportViewModel

class ReportActivity : AppCompatActivity(),
    AddReportDialogFragment.DialogAddReportFragmentListener {

    private lateinit var binding: ActivityReportBinding
    private val viewModel: ReportViewModel by viewModels()
    var editReportDialogItemPosition = -1

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

        setupFonts()

        val date = intent.getStringExtra("date")
        val km = intent.getLongExtra("km", 0).toString() + " km"
        val licencePlateNr = intent.getStringExtra("licencePlateNr")

        binding.licencePlateNrTV.text = licencePlateNr
        binding.titleKm.text = km
        binding.titleDate.text = date

        binding.standardFab.setOnClickListener {
            editReportDialogItemPosition = -1
            showDialog()
        }

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
                    logoutUser()
                    return@setNavigationItemSelectedListener true
                }

                else -> return@setNavigationItemSelectedListener super.onOptionsItemSelected(
                    menuItem
                )
            }
        }

        updateNavigationHeader(this)
    }

    override fun onReportInfoAdded(report: ReportInfoEntity) {
        if (editReportDialogItemPosition == -1)
            viewModel.addReport(report)
        else {
            viewModel.updateReport(editReportDialogItemPosition, report)
        }
        updateListUI()
    }

    private fun updateListUI() {
        if (viewModel.reports.value?.isNotEmpty() == true) {
            binding.emptyTextLL.visibility = View.GONE
            binding.reportRecyclerView.visibility = View.VISIBLE
        } else {
            binding.emptyTextLL.visibility = View.VISIBLE
            binding.reportRecyclerView.visibility = View.GONE
        }

        val onEditClick: (Int) -> Unit = { position ->
            editReportDialogItemPosition = position

            val reportItem = viewModel.reports.value!![position]
            showDialog(reportItem)
        }

        viewModel.sortReports()

        val adapter = ReportAdapter(viewModel.reports.value!!, onEditClick, this)
        binding.reportRecyclerView.adapter = adapter
    }

    private fun showDialog(reportItem: ReportInfoEntity? = null) {
        val dialog = AddReportDialogFragment()

        if (reportItem != null) {
            val bundle = Bundle()
            bundle.putString("km", reportItem.speedometerValue.toString())
            bundle.putString("date", reportItem.date)
            bundle.putString("pricePerUnit", reportItem.fuelPrice.toString())
            bundle.putString("quantity", reportItem.fuelAmount.toString())
            dialog.arguments = bundle
        }
        dialog.show(supportFragmentManager, "AddReportDialogFragment")
    }

    fun logoutUser() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun setupFonts() {
        binding.licencePlateNrTV.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
        binding.titleKm.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.titleDate.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.finishReportBtn.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
        binding.emptyTV.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
    }


    override fun onResume() {
        super.onResume()
        // Clear selected item in the navigation drawer
        binding.navView.menu.forEachIndexed { index, item ->
            item.isChecked = false
        }
    }

    fun updateNavigationHeader(context: Context) {
        val headerView = binding.navView.getHeaderView(0)
        val fullName = "John Doe"
        headerView.findViewById<TextView>(R.id.nav_header_title).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_subtitle).text = "01/01/2003"
    }
}