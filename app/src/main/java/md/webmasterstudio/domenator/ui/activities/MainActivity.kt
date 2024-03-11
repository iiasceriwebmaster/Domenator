package md.webmasterstudio.domenator.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEachIndexed
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.entity.CarInfo
import md.webmasterstudio.domenator.databinding.ActivityMainBinding
import md.webmasterstudio.domenator.ui.activities.login.LoginActivity
import md.webmasterstudio.domenator.ui.fragments.AddCarInfoDialogFragment

class MainActivity : AppCompatActivity(), AddCarInfoDialogFragment.DialogAddCarFragmentListener {

    private lateinit var binding: ActivityMainBinding

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

        binding.startReportBtn.setOnClickListener {
            val dialog = AddCarInfoDialogFragment()
            dialog.show(supportFragmentManager, "AddCarInfoDialogFragment")

        }

        binding.leftButton.setOnClickListener {
            binding.drawerLayout.open()
        }

        binding.rightButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        binding.navView.menu.getItem(0).isChecked = false
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

    override fun onResume() {
        super.onResume()
        // Clear selected item in the navigation drawer
        binding.navView.menu.forEachIndexed { index, item ->
            item.isChecked = false
        }
    }

    override fun onCarInfoAdded(carInfoItem: CarInfo) {
        val intent = Intent(this, CarReceptionActivity::class.java)

        intent.putExtra("km", carInfoItem.speedometerValue)
        intent.putExtra("date", carInfoItem.date)
        intent.putExtra("licencePlateNr", carInfoItem.licencePlateNr)

        startActivity(intent)
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
