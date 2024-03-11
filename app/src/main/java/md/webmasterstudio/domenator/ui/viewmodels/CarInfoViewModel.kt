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

    private val _selectedPhotos = MutableLiveData<MutableList<Uri>>()
    private val _selectedDocuments = MutableLiveData<MutableList<Uri>>()

    val selectedPhotos: LiveData<MutableList<Uri>>
        get() = _selectedPhotos

    val selectedDocuments: LiveData<MutableList<Uri>>
        get() = _selectedDocuments

    fun addSelectedImage(imageUri: Uri, isDocument: Boolean) {
        if (isDocument) {
            val documents = _selectedDocuments.value?.toMutableList() ?: mutableListOf()
            documents.add(imageUri)
            _selectedDocuments.value = documents
        } else {
            val photos = _selectedPhotos.value?.toMutableList() ?: mutableListOf()
            photos.add(imageUri)
            _selectedPhotos.value = photos
        }
    }

    fun removeSelectedImage(imageUri: Uri, isDocument: Boolean) {
        if (isDocument) {
            val documents = _selectedDocuments.value?.toMutableList() ?: return
            documents.remove(imageUri)
            _selectedDocuments.value = documents
        } else {
            val photos = _selectedPhotos.value?.toMutableList() ?: return
            photos.remove(imageUri)
            _selectedPhotos.value = photos
        }
    }
}
