package com.example.aperture.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aperture.R
import com.example.aperture.data.jsonclasses.PhotoCollection
import com.example.aperture.data.bdentities.PhotoCollectionEntity
import com.example.aperture.data.repositories.PhotoRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.launch

class CollectionsFragmentViewModel(val app: Application) : AndroidViewModel(app) {
    private val photoRepository = PhotoRepository(app)
    private val collectionsLiveData = MutableLiveData<List<PhotoCollection>>()
    private val collectionsBdLiveData = MutableLiveData<List<PhotoCollectionEntity>>()
    val snackBarLiveData = SingleLiveEvent<String>()
    private var currentPage = 1


    val collections: LiveData<List<PhotoCollection>>
        get() = collectionsLiveData

    val collectionsBd: LiveData<List<PhotoCollectionEntity>>
        get() = collectionsBdLiveData


    fun getFirstPage() {
        getCollections(1)
    }

    fun onMoreCollections() {
        currentPage++
        getCollections(currentPage)
    }

    private fun getCollections(page: Int) {
        viewModelScope.launch {
            val collections = photoRepository.getCollections(page)
            if (collections == null) {
                onLoadingError()
                return@launch
            }
            collectionsLiveData.postValue(
                    if (page == 1) {
                        collections
                    } else {
                        collectionsLiveData.value.orEmpty() + collections
                    }
            )
            if (page == 1) {
                saveCollections(collections)
            }
        }
    }


    private suspend fun onLoadingError() {
        val collections = photoRepository.getCollectionsFromBd().orEmpty()
        if (collections.isEmpty()) {
            showMessage(app.getString(R.string.snackBarNetworkError))
        } else {
            showMessage(app.getString(R.string.snackBarLoadBd))
        }
        collectionsBdLiveData.postValue(collections)
    }

    private suspend fun saveCollections(list: List<PhotoCollection>) {
        photoRepository.savePhotoCollectionsToBd(list)
    }


    private fun showMessage(message: String) {
        snackBarLiveData.postValue(message)
    }


}