package md.webmasterstudio.domenator.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CarInfoViewModel : ViewModel() {
    private val _date = MutableLiveData<String>()
    private val _km = MutableLiveData<String>()
    private val _licencePlateNr = MutableLiveData<String>()

    val date: LiveData<String>
        get() = _date

    val km: LiveData<String>
        get() = _km
    val licencePlateNr: LiveData<String>
        get() = _licencePlateNr

    fun setDate(date: String) {
        _date.value = date
    }

    fun setKm(km: String) {
        _km.value = km
    }

    fun setLicensePlateNr(licencePlateNr: String) {
        _licencePlateNr.value = licencePlateNr
    }
}