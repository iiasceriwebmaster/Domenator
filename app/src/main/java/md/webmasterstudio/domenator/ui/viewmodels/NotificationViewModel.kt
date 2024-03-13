package md.webmasterstudio.domenator.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import md.webmasterstudio.domenator.data.db.dao.NotificationDao
import md.webmasterstudio.domenator.data.db.entity.NotificationEntity

class NotificationViewModel(private val notificationDao: NotificationDao): ViewModel() {
    val allNotifications: LiveData<List<NotificationEntity>> = notificationDao.getAllNotifications().asLiveData()

    fun insert(notification: NotificationEntity) {
        viewModelScope.launch {
            notificationDao.insert(notification)
        }
    }

    fun getAllNotifications(): List<NotificationEntity>? {
        return allNotifications.value
    }
}