package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import md.webmasterstudio.domenator.data.db.dao.UserDao
import md.webmasterstudio.domenator.data.db.entity.User
import md.webmasterstudio.domenator.networking.ApiClient

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    fun getUser(userId: Long) {
        viewModelScope.launch {
            // Check for cached user in Room first
            val cachedUser = userDao.getUserById(userId)
            if (cachedUser != null) {
                _user.value = cachedUser
                return@launch // Exit if cached user found
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val service = ApiClient.instance?.userService

            // Fetch user from network if not cached
            try {
                val userResponse = service?.login(email, password)
                _user.value = userResponse
                userResponse?.let { userDao.insertUser(it) } // Store in Room for future access
            } catch (e: Exception) {
                // Handle network error
                _user.value = null
            }
        }
    }
}