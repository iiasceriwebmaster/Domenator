package md.webmasterstudio.domenator.md.webmasterstudio.domenator.activities.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import md.webmasterstudio.domenator.data.LoginDataSource
import md.webmasterstudio.domenator.data.LoginRepository

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                loginRepository = LoginRepository(
                    dataSource = LoginDataSource()
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}