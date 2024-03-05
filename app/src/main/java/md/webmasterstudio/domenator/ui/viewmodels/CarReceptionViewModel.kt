package md.webmasterstudio.domenator.md.webmasterstudio.domenator.viewmodels

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CarReceptionViewModel : ViewModel() {
    private val _selectedPhotos = MutableLiveData<List<Uri>>()
    private val _selectedDocuments = MutableLiveData<List<Uri>>()

    val selectedPhotos: LiveData<List<Uri>>
        get() = _selectedPhotos

    val selectedDocuments: LiveData<List<Uri>>
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
