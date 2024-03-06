package md.webmasterstudio.domenator.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.forEachIndexed
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

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
            val intent = Intent(this, CarReceptionActivity::class.java)
            startActivity(intent)
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
                R.id.nav_personal_cabinet -> {
                    // Open personal cabinet
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
}
