package com.example.aperture.ui

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aperture.R
import com.example.aperture.data.*
import com.example.aperture.data.jsonclasses.Photo
import com.example.aperture.data.jsonclasses.PhotoCollection
import com.example.aperture.data.jsonclasses.PhotoWrapper
import com.example.aperture.data.jsonclasses.Profile
import com.example.aperture.data.repositories.AuthRepository
import com.example.aperture.data.repositories.PhotoRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.launch

class MyProfileFragmentViewModel(val app: Application) : AndroidViewModel(app) {
    private val photoRepository = PhotoRepository(app)
    private val authRepository = AuthRepository(app)
    private val profileLiveData = MutableLiveData<Profile>()
    private val userPhotosLiveData = MutableLiveData<List<Photo>>()
    private val userLikedPhotosLiveData = MutableLiveData<List<Photo>>()
    private val userCollectionsLiveData = MutableLiveData<List<PhotoCollection>>()
    private val profileImageUrlLiveData = MutableLiveData<String>()
    private val snackbarLiveData = SingleLiveEvent<String>()
    private val logOutLiveData = SingleLiveEvent<Unit>()
    private val photoChangedLiveData = MutableLiveData<PhotoWrapper>()
    private val buttonEnabledLiveData = MutableLiveData<Int>()
    private val onLoadingErrorLiveData = SingleLiveEvent<Unit>()
    private var currentPage = 1


    val profile: LiveData<Profile>
        get() = profileLiveData

    val userPhotos: LiveData<List<Photo>>
        get() = userPhotosLiveData

    val userLikedPhotos: LiveData<List<Photo>>
        get() = userLikedPhotosLiveData

    val userCollections: LiveData<List<PhotoCollection>>
        get() = userCollectionsLiveData

    val profileImageUrl: LiveData<String>
        get() = profileImageUrlLiveData

    val snackbar: LiveData<String>
        get() = snackbarLiveData

    val logOut: LiveData<Unit>
        get() = logOutLiveData

    val photoChanged: LiveData<PhotoWrapper>
        get() = photoChangedLiveData

    val buttonEnabled: LiveData<Int>
        get() = buttonEnabledLiveData

    val onLoadinError: LiveData<Unit>
        get() = onLoadingErrorLiveData


    fun getProfileInfo() {
        viewModelScope.launch {
            val profile = photoRepository.getMyProfile()
            val extendedProfile = photoRepository.getProfileByUrl(profile?.links?.self.orEmpty())
            if (profile == null || extendedProfile == null) {
                onLoadingError()
                return@launch
            }
            profileLiveData.postValue(profile)
            profileImageUrlLiveData.postValue(extendedProfile.profile_image?.small)
            onFirstUserPhotos()
        }

    }

    private fun onLoadingError() {
        showMessage(app.getString(R.string.snackBarNetworkError))
        onLoadingErrorLiveData.postValue(Unit)
    }

    private fun getUserPhotos(page: Int) {
        viewModelScope.launch {
            val username = photoRepository.getMyProfile()?.username
            val userPhotos = photoRepository.getUserPhotos(username.orEmpty(), page)
            if (userPhotos == null) {
                onLoadingError()
                return@launch
            }
            userPhotosLiveData.postValue(
                    if (page == 1) {
                        userPhotos
                    } else {
                        userPhotosLiveData.value.orEmpty() + userPhotos
                    }
            )

        }
    }

    private fun getUserLikedPhotos(page: Int) {
        viewModelScope.launch {
            val photos = photoRepository.getUserLikedPhotos(profileLiveData.value?.username.orEmpty(), page)

            if (photos == null) {
                onLoadingError()
                return@launch
            }
            userLikedPhotosLiveData.postValue(
                    if (page == 1) {
                        photos
                    } else {
                        userLikedPhotosLiveData.value.orEmpty() + photos
                    }
            )


        }
    }

    private fun getUserCollections(page: Int) {
        viewModelScope.launch {
            val collections = photoRepository.getUserCollections(profileLiveData.value?.username.orEmpty(), page)

            if (collections == null) {
                onLoadingError()
                return@launch
            }
            userCollectionsLiveData.postValue(
                    if (page == 1) {
                        collections
                    } else {
                        userCollectionsLiveData.value.orEmpty() + collections
                    }
            )


        }
    }

    fun onMore() {
        currentPage++
        when (buttonEnabledLiveData.value) {
            1 -> getUserPhotos(currentPage)
            2 -> getUserLikedPhotos(currentPage)
            3 -> getUserCollections(currentPage)
        }

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

    fun onFirstUserPhotos() {
        photoChangedLiveData.postValue(null)
        getUserPhotos(1)
        buttonEnabledLiveData.postValue(1)
    }

    fun onFirstLikedPhotos() {
        photoChangedLiveData.postValue(null)
        getUserLikedPhotos(1)
        buttonEnabledLiveData.postValue(2)

    }

    fun onFirstUserCollections() {
        getUserCollections(1)
        buttonEnabledLiveData.postValue(3)

    }


    fun onLogout() {
        viewModelScope.launch {
            authRepository.logOut()
            photoRepository.deleteAllDataFromBd()
            logOutLiveData.postValue(Unit)

        }
    }


    fun getButtonEnabled(): Int {
        return buttonEnabledLiveData.value ?: 0
    }


    private fun showMessage(message: String) {
        snackbarLiveData.postValue(message)
    }

}