package md.webmasterstudio.domenator.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEachIndexed
import androidx.lifecycle.lifecycleScope
import com.daimajia.androidanimations.library.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.DomenatorDatabase
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.databinding.ActivityMainBinding
import md.webmasterstudio.domenator.ui.activities.login.LoginActivity
import md.webmasterstudio.domenator.ui.fragments.AddCarInfoDialogFragment
import md.webmasterstudio.domenator.ui.viewmodels.CarInfoViewModel

class MainActivity : AppCompatActivity(), AddCarInfoDialogFragment.DialogAddCarFragmentListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var carInfoViewModel: CarInfoViewModel
    private lateinit var appDatabase: DomenatorDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        appDatabase = DomenatorDatabase.getInstance(applicationContext)
        carInfoViewModel = CarInfoViewModel(appDatabase.carInfoDao(), appDatabase.imageDao())

        //TODO: Fix bugs
        binding.startReportBtn.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val carInfoEntities = carInfoViewModel.getCarInfoEntities()
                    if (carInfoEntities.isNotEmpty()) {
                        val intent = Intent(this@MainActivity, ReportActivity::class.java)
                        intent.putExtra("date", carInfoEntities[0].date)
                        intent.putExtra("km", carInfoEntities[0].speedometerValue)
                        intent.putExtra("licencePlateNr", carInfoEntities[0].licencePlateNr)
                        startActivity(intent)
                    } else {
                        val dialog = AddCarInfoDialogFragment()
                        dialog.show(supportFragmentManager, "AddCarInfoDialogFragment")
                    }
                }
            }
        }

        binding.leftButton.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.rightButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.close()
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

        setupFonts()
    }

    fun logoutUser() {
        // Perform logout action, such as clearing user session and navigating to the login screen
        // For example:
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Finish the MainActivity to prevent returning to it after logout
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.activity_main_drawer, menu)
        return true
    }

    override fun onCarInfoAdded(carInfoEntityItem: CarInfoEntity) {
        val intent = Intent(this, CarReceptionActivity::class.java)

        intent.putExtra("km", carInfoEntityItem.speedometerValue)
        intent.putExtra("date", carInfoEntityItem.date)
        intent.putExtra("licencePlateNr", carInfoEntityItem.licencePlateNr)

        startActivity(intent)
    }

    private fun setupFonts() {
        binding.mainText.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.startReportBtn.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    fun updateNavigationHeader(context: Context) {
        val headerView = binding.navView.getHeaderView(0)
        val fullName = "John Doe"
        headerView.findViewById<TextView>(R.id.nav_header_title).text = fullName
        headerView.findViewById<TextView>(R.id.nav_header_subtitle).text = "01/01/2003"
    }
}
