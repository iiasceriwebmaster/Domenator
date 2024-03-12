package md.webmasterstudio.domenator.ui.activities.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import md.webmasterstudio.domenator.R
import md.webmasterstudio.domenator.data.LoginRepository
import md.webmasterstudio.domenator.data.Result
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login.LoggedInUserView
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login.LoginFormState
import md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login.LoginResult
class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(username, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(
                    success = LoggedInUserView(
                        name = result.data.name,
                        surname = result.data.surname,
                        dateOfBirth = result.data.dateOfBirth,
                        email = result.data.email
                    )
                )
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(username: String, password: String) {
        val currentState = _loginForm.value
        val newState = if (!isUserNameValid(username)) {
            LoginFormState(usernameError = R.string.invalid_email)
        } else if (!isPasswordValid(password)) {
            LoginFormState(passwordError = R.string.invalid_password)
        } else {
            LoginFormState(isDataValid = true)
        }

        // Reset the errors when language is changed
        if (currentState?.usernameError != newState.usernameError) {
            newState.usernameError = null
        }

        if (currentState?.passwordError != newState.passwordError) {
            newState.passwordError = null
        }

        // Only update the login form state if it's different from the current state
        if (currentState != newState) {
            _loginForm.value = newState
        }
    }

    // A placeholder email validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}