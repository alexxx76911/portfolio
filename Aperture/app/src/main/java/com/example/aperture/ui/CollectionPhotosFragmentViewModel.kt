package com.example.aperture.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import com.example.aperture.R
import com.example.aperture.data.*
import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.data.jsonclasses.Photo
import com.example.aperture.data.jsonclasses.PhotoCollection
import com.example.aperture.data.jsonclasses.PhotoWrapper
import com.example.aperture.data.jsonclasses.Tag
import com.example.aperture.data.repositories.PhotoRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.launch

class CollectionPhotosFragmentViewModel(val app: Application) : AndroidViewModel(app) {
    private val photoRepository = PhotoRepository(app)
    private val collectionLiveData = MutableLiveData<PhotoCollection>()
    private val collectionTagsLiveData = MutableLiveData<List<Tag>>()
    private val collectionPhotosLiveData = MutableLiveData<List<Photo>>()
    private val collectionPhotosBdLiveData = MutableLiveData<List<PhotoEntity>>()
    private val collectionPhotoChangedLiveData = MutableLiveData<PhotoWrapper>()
    private val collectionBdLiveData = MutableLiveData<PhotoCollectionEntity>()
    private val snackBarLiveData = SingleLiveEvent<String>()
    private var currentPage = 1
    private lateinit var collectionId: String
    private var collectionPosition: Int = 0


    val collection: LiveData<PhotoCollection>
        get() = collectionLiveData

    val collectionBd: LiveData<PhotoCollectionEntity>
        get() = collectionBdLiveData

    val collectionPhotos: LiveData<List<Photo>>
        get() = collectionPhotosLiveData

    val collectionPhotosBd: LiveData<List<PhotoEntity>>
        get() = collectionPhotosBdLiveData

    val collectionTags: LiveData<List<Tag>>
        get() = collectionTagsLiveData

    val collectionPhotoChanged: LiveData<PhotoWrapper>
        get() = collectionPhotoChangedLiveData

    val snackbar: LiveData<String>
        get() = snackBarLiveData

    fun getCollectionInfo(id: String, position: Int) {
        collectionId = id
        collectionPosition = position
        viewModelScope.launch {
            val collection = photoRepository.getCollectionById(id)
            val tags = photoRepository.getPhotoById(collection?.cover_photo?.id ?: "")?.tags

            if (collection == null || tags == null) {
                photoRepository.getCollectionFromBd(id)?.let {
                    collectionBdLiveData.postValue(it)
                }

                return@launch
            }
            collectionLiveData.postValue(collection)
            collectionTagsLiveData.postValue(tags)

        }

    }

    private fun getCollectionPhotos(page: Int) {
        viewModelScope.launch {
            val photos = photoRepository.getCollectionPhotos(collectionId, page)
            if (photos == null) {
                onLoadingError()
                return@launch
            }
            collectionPhotosLiveData.postValue(
                    if (page == 1) {
                        photos
                    } else {
                        collectionPhotosLiveData.value.orEmpty() + photos
                    }
            )
            if (collectionPosition < 11) {
                savePhotos(photos)
            }

        }
    }

    fun getFirstCollectionPhotos() {
        collectionPhotoChangedLiveData.postValue(null)
        getCollectionPhotos(1)
    }

    fun onMore() {
        currentPage++
        getCollectionPhotos(currentPage)
    }


    private suspend fun savePhotos(photos: List<Photo>) {
        photoRepository.savePhotosToBd(photos, collectionId)
    }

    private suspend fun onLoadingError() {
        val photos = photoRepository.getCollectionPhotosFromBd(collectionId).orEmpty()
        if (photos.isEmpty()) {
            showMessage(app.getString(R.string.snackBarNetworkError))
        } else {
            showMessage(app.getString(R.string.snackBarLoadBd))
        }
        collectionPhotosBdLiveData.postValue(photos)
    }

    fun onLikeClick(id: String, liked: Boolean) {
        viewModelScope.launch {
            val photo = if (liked) {
                photoRepository.unlikePhoto(id)
            } else {
                photoRepository.likePhoto(id)
            }
            if (photo == null) {
                showMessage(app.getString(R.string.snackBarNetworkError))
                return@launch
            }
            collectionPhotoChangedLiveData.postValue(photo)

        }
    }

    private fun showMessage(message: String) {
        snackBarLiveData.postValue(message)
    }

}