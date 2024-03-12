package md.webmasterstudio.domenator.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import md.webmasterstudio.domenator.data.db.entity.User
import md.webmasterstudio.domenator.databinding.ActivityUserProfileBinding

fun getMockUserData(): User {
    // Mock user data
    return User(
        id = 1,
        firstName = "John",
        lastName = "Doe",
        email = "john.doe@example.com",
        dateOfBirth = "1990-01-01" // Format as needed
    )
}

class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.leftButton.setOnClickListener {
            onBackPressed()
        }

        // Mock user data
        val user = getMockUserData()

        // Set user data to TextViews using View Binding
        binding.textViewName.text = "Name: ${user.firstName}"
        binding.textViewSurname.text = "Surname: ${user.lastName}"
        binding.textViewEmail.text = "Email: ${user.email}"
        binding.textViewDOB.text = "Date of Birth: ${user.dateOfBirth}"

    }
}