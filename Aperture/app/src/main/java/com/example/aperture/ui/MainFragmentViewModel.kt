package com.example.aperture.ui

import android.app.Application

import androidx.lifecycle.*
import com.example.aperture.R
import com.example.aperture.data.*
import com.example.aperture.data.bdentities.PhotoEntity
import com.example.aperture.data.jsonclasses.Photo
import com.example.aperture.data.jsonclasses.PhotoWrapper
import com.example.aperture.data.repositories.AuthRepository
import com.example.aperture.data.repositories.PhotoRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainFragmentViewModel(val app: Application) : AndroidViewModel(app) {
    private val photoRepository = PhotoRepository(app)
    private val authRepository = AuthRepository(app)
    private val photosLiveData = MutableLiveData<List<Photo>>()
    private val photosBdLiveData = MutableLiveData<List<PhotoEntity>>()
    private val isLoggedLiveData = SingleLiveEvent<Boolean>()
    private val photoChangedLiveData = MutableLiveData<PhotoWrapper>()
    val snackBarLiveData = SingleLiveEvent<String>()
    private var currentPage = 1
    private var searchJob: Job? = null


    val photos: LiveData<List<Photo>>
        get() = photosLiveData

    val photosBd: LiveData<List<PhotoEntity>>
        get() = photosBdLiveData

    val isLogged: LiveData<Boolean>
        get() = isLoggedLiveData

    val photoChanged: LiveData<PhotoWrapper>
        get() = photoChangedLiveData


    private fun getPhotos(page: Int) {
        viewModelScope.launch {
            val photos = photoRepository.getPhotos(page)
            if (photos == null) {
                onLoadingError()
                return@launch
            }
            photosLiveData.postValue(
                    if (page == 1) {
                        photos
                    } else {
                        photosLiveData.value.orEmpty() + photos
                    }
            )
            if (page == 1) {
                savePhotosToBd(photos)
            }
        }
    }


    fun onMorePhotos() {
        currentPage++
        getPhotos(currentPage)
    }

    fun getFirstPage() {
        photoChangedLiveData.postValue(null)
        getPhotos(1)
    }


    private suspend fun onLoadingError() {
        val photos = photoRepository.getCollectionPhotosFromBd("").orEmpty()
        if (photos.isEmpty()) {
            showMessage(app.getString(R.string.snackBarNetworkError))
        } else {
            showMessage(app.getString(R.string.snackBarLoadBd))
        }
        photosBdLiveData.postValue(photos)

    }

    private suspend fun savePhotosToBd(list: List<Photo>) {
        photoRepository.savePhotosToBd(list, null)
    }


    fun onLikeClick(id: String, liked: Boolean) {
        viewModelScope.launch {
            val photo = if (liked) {
                photoRepository.unlikePhoto(id)
            } else {
                photoRepository.likePhoto(id)
            }
            photo ?: showMessage(app.getString(R.string.snackBarNetworkError))
            photo?.let {
                photoChangedLiveData.postValue(it)
            }

        }
    }


    fun isLogged() {
        viewModelScope.launch {
            val token = authRepository.getSavedToken()
            if (token != null) {
                AccessToken.token = "Bearer $token"
                isLoggedLiveData.postValue(true)
            } else {
                isLoggedLiveData.postValue(false)
            }


        }
    }

    fun collectSearchFlow(flow: Flow<String>) {
        searchJob = flow
                .debounce(3000)
                .mapLatest { query ->
                    photoRepository.searchPhotos(query)
                }
                .onEach { result ->
                    result?.let {
                        photosLiveData.postValue(it.results)
                    }
                    result ?: showMessage(app.getString(R.string.snackBarNetworkError))

                }.launchIn(viewModelScope)

    }

    private fun showMessage(message: String) {
        snackBarLiveData.postValue(message)
    }


    fun cancelSearchJob() {
        searchJob?.cancel()
    }


}