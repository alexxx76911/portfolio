package com.example.aperture.ui

import android.app.Application
import android.content.Intent
import androidx.lifecycle.*
import androidx.work.*
import com.example.aperture.R
import com.example.aperture.data.jsonclasses.Photo

import com.example.aperture.data.repositories.PhotoRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.launch

class PhotoDetailsFragmentViewModel(val app: Application) : AndroidViewModel(app) {
    private val photoRepository = PhotoRepository(app)
    private val photoInfoLiveData = MutableLiveData<Photo>()
    private val errorLiveData = SingleLiveEvent<Unit>()
    private val snackBarLiveData = SingleLiveEvent<String>()
    private val shareIntentLiveData = SingleLiveEvent<Intent>()
    var workInfoLiveData: LiveData<WorkInfo>? = null


    val photoInfo: LiveData<Photo>
        get() = photoInfoLiveData

    val error: LiveData<Unit>
        get() = errorLiveData

    val snackBar: LiveData<String>
        get() = snackBarLiveData

    val shareIntent: LiveData<Intent>
        get() = shareIntentLiveData


    fun getPhotoInfo(id: String) {
        viewModelScope.launch {
            val photo = photoRepository.getPhotoById(id)
            if (photo != null) {
                photoInfoLiveData.postValue(photo)
            } else {
                onLoadingError()
            }
        }
    }


    private fun onLoadingError() {
        showMessage(app.getString(R.string.snackBarNetworkError))
        errorLiveData.postValue(Unit)
    }


    fun onLikeClick(id: String) {
        viewModelScope.launch {
            val photo = photoRepository.getPhotoById(id)
            if (photo == null) {
                showMessage(app.getString(R.string.snackBarNetworkError))
                return@launch
            }
            val updatedPhoto = if (photo.liked_by_user) {
                photoRepository.unlikePhoto(photo.id)
            } else {
                photoRepository.likePhoto(photo.id)
            }

            if (updatedPhoto == null) {
                showMessage(app.getString(R.string.snackBarNetworkError))
            } else {
                photoInfoLiveData.postValue(updatedPhoto.photo)
            }
        }
    }

    fun sharePhoto(id: String) {
        shareIntentLiveData.postValue(photoRepository.getShareIntentById(id))
    }

    fun savePhoto(id: String) {
        val photo = photoInfoLiveData.value ?: return

        viewModelScope.launch {
            photoRepository.getPhotoById((photo.id))
                ?: showMessage(app.getString(R.string.snackBarDownloadError))
        }

        val fileName = "${(photo.id)}.jpg"
        val url = photo.urls.raw

        workInfoLiveData = photoRepository.startDownloadWork(fileName, url, id)

    }


    private fun showMessage(message: String) {
        snackBarLiveData.postValue(message)
    }


}