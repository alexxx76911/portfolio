package com.skillbox.github.ui.repository_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.Repository

class RepositoryListViewModel : ViewModel() {

    private val reposLiveData = MutableLiveData<List<Repository>?>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val repository = ReposListRepository()

    val repos: LiveData<List<Repository>?>
        get() = reposLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    fun getRepos() {
        repository.getRepos({ newRepos ->
            reposLiveData.postValue(newRepos)

        }, { error ->
            errorLiveData.postValue(error)
        })


    }

}