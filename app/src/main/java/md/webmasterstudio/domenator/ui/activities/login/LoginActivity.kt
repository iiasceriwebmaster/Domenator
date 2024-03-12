package md.webmasterstudio.domenator.ui.activities.login

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import md.webmasterstudio.domenator.data.db.DomenatorDatabase
import md.webmasterstudio.domenator.databinding.ActivityLoginBinding
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login.LoggedInUserView
import md.webmasterstudio.domenator.ui.activities.MainActivity
import md.webmasterstudio.domenator.ui.viewmodels.UserViewModel
import java.util.Locale

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var appDatabase: DomenatorDatabase

    private fun setLocale(lang: String) {
        val currentLocale = resources.configuration.locale
        val newLocale = Locale(lang)
        if (currentLocale != newLocale) {
            Locale.setDefault(newLocale)
            val config = Configuration()
            config.locale = newLocale
            resources.updateConfiguration(config, resources.displayMetrics)
            // Clear error fields if locale is changed
            binding.username.error = null
            binding.password.error = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.roLangBtn.setOnClickListener {
            setLocale("ro")
            recreate()
        }

        binding.ruLangBtn.setOnClickListener {
            setLocale("ru")
            recreate()
        }

        binding.enLangBtn.setOnClickListener {
            setLocale("en")
            recreate()
        }

        // Clear error fields after recreation
        binding.username.error = null
        binding.password.error = null

        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginBtn = binding.login
        val loadingProgressBar = binding.loading

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            loginBtn.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                usernameEditText.error = getString(loginState.usernameError!!)
            }
            if (loginState.passwordError != null) {
                passwordEditText.error = getString(loginState.passwordError!!)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            loadingProgressBar.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        usernameEditText.afterTextChanged {
            loginViewModel.loginDataChanged(
                usernameEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        passwordEditText.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    usernameEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            usernameEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                }
                false
            }

            loginBtn.setOnClickListener {
                loadingProgressBar.visibility = View.VISIBLE
                loginViewModel.login(usernameEditText.text.toString(), passwordEditText.text.toString())
            }
        }

        appDatabase = DomenatorDatabase.getInstance(applicationContext)
        userViewModel = UserViewModel(appDatabase.userDao())

        val email = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()
        //TODO: when api done test:
//        userViewModel.login(email, password)
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        //TODO: sqlite etc.
        
        // Start the new activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}