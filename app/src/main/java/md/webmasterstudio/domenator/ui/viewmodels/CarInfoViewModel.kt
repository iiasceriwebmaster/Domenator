package md.webmasterstudio.domenator.ui.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import md.webmasterstudio.domenator.data.db.dao.CarInfoDao
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.utilities.FileUtilities.saveImageToDevice

class CarInfoViewModel(val carInfoDao: CarInfoDao) : ViewModel() {
    private val _date = MutableLiveData<String>()
    private val _km = MutableLiveData<String>()
    private val _licencePlateNr = MutableLiveData<String>()

    fun insert(carInfoEntity: CarInfoEntity) {
        viewModelScope.launch {
            carInfoDao.insert(carInfoEntity)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            carInfoDao.deleteAll()
        }
    }

    fun getCarInfoEntities() : List<CarInfoEntity> {
        return carInfoDao.getAll()
    }

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

    // Initialize LiveData in ViewModel
    // Initialize LiveData in ViewModel constructor
    val selectedPhotos: MutableLiveData<MutableList<Uri>> = MutableLiveData(mutableListOf())
    val selectedDocuments: MutableLiveData<MutableList<Uri>> = MutableLiveData(mutableListOf())


    fun addSelectedImage(imageUri: Uri, isDocument: Boolean) {
        if (isDocument) {
            val documents = selectedDocuments.value?.toMutableList() ?: mutableListOf()
            documents.add(imageUri)
            selectedDocuments.value = documents
        } else {
            val photos = selectedPhotos.value?.toMutableList() ?: mutableListOf()
            photos.add(imageUri)
            selectedPhotos.value = photos
        }
    }

    fun removeSelectedImage(imageUri: Uri, isDocument: Boolean) {
        if (isDocument) {
            val documents = selectedDocuments.value?.toMutableList() ?: return
            documents.remove(imageUri)
            selectedDocuments.value = documents
        } else {
            val photos = selectedPhotos.value?.toMutableList() ?: return
            photos.remove(imageUri)
            selectedPhotos.value = photos
        }
    }

    fun saveCarInfo(
        date: String,
        licencePlateNr: String,
        speedometerValue: Long,
        context: Context
    ) {
        val coroutineScope = viewModelScope
        coroutineScope.launch {
            if (!selectedPhotos.value.isNullOrEmpty() || !selectedDocuments.value.isNullOrEmpty()) {
                val photoPaths = mutableListOf<String>()
                for (uri in selectedPhotos.value!!) {
                    photoPaths.add(saveImageToDevice(context, uri, false))
                }
                val documentPaths = mutableListOf<String>()
                for (uri in selectedDocuments.value!!) {
                    documentPaths.add(saveImageToDevice(context, uri, true))
                }
                val carInfoEntity = CarInfoEntity(
                    date = date,
                    licencePlateNr = licencePlateNr,
                    speedometerValue = speedometerValue,
                    photoPaths = photoPaths,
                    documentPaths = documentPaths
                )
                carInfoDao.insert(carInfoEntity)
            } else {
                Log.e("Domenator saveCarInfo", "!selectedPhotos.value.isNullOrEmpty() || !selectedDocuments.value.isNullOrEmpty()")
            }
        }
    }
}
