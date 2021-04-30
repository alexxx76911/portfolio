package com.skillbox.github.ui.repository_details


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class RepoDetailsViewModel : ViewModel() {

    private val starLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val repository = RepoDetailRepository()


    val starInfo: LiveData<Boolean>
        get() = starLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData


    fun getStarInfo(owner: String, repo: String) {
        repository.getStarInfo(owner, repo, { isStarred ->
            starLiveData.postValue(isStarred)

        }, { error ->

            errorLiveData.postValue(error)
        })
    }


    fun putOrRemoveStar(owner: String, repo: String, isStarred: Boolean) {

        if (!isStarred) {

            repository.removeStarForRepo(owner, repo) { error ->
                errorLiveData.postValue(error)
            }
        } else {

            repository.putStarToRepo(owner, repo) { error ->
                errorLiveData.postValue(error)
            }
        }
    }
}