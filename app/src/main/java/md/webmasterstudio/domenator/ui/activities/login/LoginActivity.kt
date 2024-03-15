package md.webmasterstudio.domenator.ui.activities.login

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import kotlinx.coroutines.launch
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.db.DomenatorDatabase
import md.webmasterstudio.domenator.databinding.ActivityLoginBinding
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login.LoggedInUserView
import md.webmasterstudio.domenator.networking.CheckInternetConnection
import md.webmasterstudio.domenator.networking.UserRepository.getDictionaryCoroutine
import md.webmasterstudio.domenator.ui.activities.MainActivity
import md.webmasterstudio.domenator.ui.viewmodels.UserViewModel
import java.util.Locale


class LoginActivity : AppCompatActivity() {

    private lateinit var internetConnection: CheckInternetConnection
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
            binding.email.error = null
            binding.password.error = null
        }
    }

    private fun adjustMargins(keyboardVisible: Boolean) {
        val newMarginTopForViewWithMargin = if (keyboardVisible) 8 else 190 // Adjust based on your XML values
        val newMarginTopForLoginBtn = if (keyboardVisible) 32 else 48  // Adjust based on your XML values

        val marginAnimationForViewWithMargin = MarginAnimation(binding.viewWithMargin, newMarginTopForViewWithMargin)
        marginAnimationForViewWithMargin.animate()

        val marginAnimationForLoginBtn = MarginAnimation(binding.loginBtn, newMarginTopForLoginBtn)
        marginAnimationForLoginBtn.animate()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.internetConnection = CheckInternetConnection(this)
         val STORAGE_PERMISSION_CODE = 101

        //perm
        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, permissions, STORAGE_PERMISSION_CODE)

        setupLanguageClicks()
        setupFonts()

        // Clear error fields after recreation
        binding.email.error = null
        binding.password.error = null

        val emailEditText: EditText = binding.email
        val passwordEditText: EditText = binding.password
        val loginBtn = binding.loginBtn
        val loadingProgressBar = binding.loading

        setupKeyboardListener()

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

//        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both email / password is valid
//            loginBtn.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                usernameEditText.error = getString(loginState.usernameError!!)
//            }
//            if (loginState.passwordError != null) {
//                passwordEditText.error = getString(loginState.passwordError!!)
//            }
//        })

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

        emailEditText.afterTextChanged {
            loginViewModel.loginDataChanged(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            )
        }

        passwordEditText.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            emailEditText.text.toString(),
                            passwordEditText.text.toString()
                        )
                }
                false
            }

            loginBtn.setOnClickListener {
                val email = emailEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (email == "" && !email.contains("@") && !email.contains(".")) {
                    emailEditText.error = getString(R.string.invalid_email)
                    applyAnimation(emailEditText)
                } else if (password.length < 5) {
                    passwordEditText.error = getString(R.string.invalid_password)
                    applyAnimation(passwordEditText)
                } else {
                    loadingProgressBar.visibility = View.VISIBLE

                    val email = emailEditText.text.toString()
                    val password = passwordEditText.text.toString()
                    //TODO: when api done test:
                    //userViewModel.login(email, password)
                    loginViewModel.login(emailEditText.text.toString(), passwordEditText.text.toString())
                }
            }
        }

        appDatabase = DomenatorDatabase.getInstance(applicationContext)
        userViewModel = UserViewModel(appDatabase.userDao())

        if (internetConnection.isOnline((this as Activity?)!!)) {
            lifecycleScope.launch {
                getDictionaryCoroutine(this, this@LoginActivity)
            }
        } else {
            internetConnection.showDefaultNoInternetConnectionAlertDialog((this as Activity?)!!)
        }
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

    private fun applyAnimation(view: View) {
        YoYo.with(Techniques.Shake)
            .duration(700)
            .playOn(view)
    }

    fun setupLanguageClicks() {
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
    }

    private fun setupFonts() {
        binding.companyNameTV.setTypeface(ResourcesCompat.getFont(this, R.font.barlowsemicondensed_semibold))
        binding.title.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_bold))
        binding.email.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.password.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
        binding.loginBtn.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_medium))
        binding.languagePickerTV.setTypeface(ResourcesCompat.getFont(this, R.font.roboto_regular))
    }

    fun setupKeyboardListener() {
        binding.container.getViewTreeObserver().addOnGlobalLayoutListener(OnGlobalLayoutListener {
            // on below line we are creating a variable for rect
            val rect = Rect()


            // on below line getting frame for our relative layout.
            binding.container.getWindowVisibleDisplayFrame(rect)


            // on below line getting screen height for relative layout.
            val screenHeight: Int = binding.container.getRootView().height


            // on below line getting keypad height.
            val keypadHeight = screenHeight - rect.bottom


            // on below line we are checking if keypad height is greater than screen height.
            if (keypadHeight > screenHeight * 0.15) {
                // displaying toast message as keyboard showing.

                Log.i(this@LoginActivity::class.java.name, "Keyboard is showing")
                adjustMargins(true)
            } else {
                // displaying toast message as keyboard closed.
                adjustMargins(false)
                Log.i(this@LoginActivity::class.java.name, "Keyboard closed")
            }
        })
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

class MarginAnimation(private val view: View, private val newMargin: Int) : Animation() {

    override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
//        layoutParams.topMargin = (newMargin * interpolatedTime).toInt()
        layoutParams.topMargin = newMargin
        view.layoutParams = layoutParams
    }

    fun animate() {
        setDuration(700)  // Adjust duration as needed
        view.startAnimation(this)
    }
}