package md.webmasterstudio.domenator.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import md.webmasterstudio.domenator.data.db.dao.CarInfoDao
import md.webmasterstudio.domenator.data.db.dao.ImageDao
import md.webmasterstudio.domenator.data.db.entity.CarInfoEntity
import md.webmasterstudio.domenator.data.db.entity.ImageModel
import md.webmasterstudio.domenator.utilities.BitmapConverter.convertBitmapToString
import md.webmasterstudio.domenator.utilities.BitmapConverter.convertStringToBitmap
import java.io.InputStream
import java.util.UUID


class CarInfoViewModel(val carInfoDao: CarInfoDao, val imageDao: ImageDao) : ViewModel() {
    private val _date = MutableLiveData<String>()
    private val _km = MutableLiveData<String>()
    private val _licencePlateNr = MutableLiveData<String>()
    val selectedPhotos: MutableLiveData<MutableList<Bitmap>> = MutableLiveData(mutableListOf())
    val selectedDocuments: MutableLiveData<MutableList<Bitmap>> = MutableLiveData(mutableListOf())

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

    fun getCarInfoEntities(): List<CarInfoEntity> {
        return carInfoDao.getAll()
    }

    fun loadImages() = runBlocking {
        val carInfoEntity = carInfoDao.getAll()[0]

        val photoIds = carInfoEntity.photoIds
        val photoBitmaps: MutableList<Bitmap> = mutableListOf()
        for (photoId in photoIds) {
            val image = imageDao.getImageById(photoId)
            val bitmap = image?.imageString?.let { convertStringToBitmap(it) }
            bitmap?.let { photoBitmaps.add(it) }
        }

        val documentIds = carInfoEntity.documentIds
        val documentBitmaps: MutableList<Bitmap> = mutableListOf()
        for (documentId in documentIds) {
            val image = imageDao.getImageById(documentId)
            val bitmap = image?.imageString?.let { convertStringToBitmap(it) }
            bitmap?.let { documentBitmaps.add(it) }
        }
        selectedPhotos.postValue(photoBitmaps)
        selectedDocuments.postValue(documentBitmaps)
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


    fun addSelectedImage(imageBitmap: Bitmap, isDocument: Boolean) {
        if (isDocument) {
            val documents = selectedDocuments.value?.toMutableList() ?: mutableListOf()
            documents.add(imageBitmap)
            selectedDocuments.value = documents
        } else {
            val photos = selectedPhotos.value?.toMutableList() ?: mutableListOf()
            photos.add(imageBitmap)
            selectedPhotos.value = photos
        }
    }

    fun removeSelectedImage(imageUri: Bitmap, isDocument: Boolean) {
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
        context: Context,
        onInsertDone: () -> Unit
    ) {
        val coroutineScope = viewModelScope
        coroutineScope.launch {
            if (!selectedPhotos.value.isNullOrEmpty() || !selectedDocuments.value.isNullOrEmpty()) {
                val photoIds = mutableListOf<String>()
                for (bitmap in selectedPhotos.value!!) {
                    val randomUID = UUID.randomUUID().toString()
                    val imageModel = ImageModel(randomUID, convertBitmapToString(bitmap))
                    runBlocking { imageDao.insertNewImage(imageModel) }

                    photoIds.add(randomUID)
                }
                val documentIds = mutableListOf<String>()
                for (bitmap in selectedDocuments.value!!) {
                    val randomUID = UUID.randomUUID().toString()

                    val imageModel = ImageModel(randomUID, convertBitmapToString(bitmap))
                    runBlocking { imageDao.insertNewImage(imageModel) }
                    documentIds.add(randomUID)
                }
                val carInfoEntity = CarInfoEntity(
                    date = date,
                    licencePlateNr = licencePlateNr,
                    speedometerValue = speedometerValue,
                    photoIds = photoIds,
                    documentIds = documentIds
                )
                carInfoDao.insert(carInfoEntity)
                onInsertDone()
            } else {
                Log.e(
                    "Domenator saveCarInfo",
                    "!selectedPhotos.value.isNullOrEmpty() || !selectedDocuments.value.isNullOrEmpty()"
                )
            }
        }
    }
}
