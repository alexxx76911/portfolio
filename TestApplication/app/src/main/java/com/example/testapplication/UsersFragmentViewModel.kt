package com.example.testapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class UsersFragmentViewModel : ViewModel() {
    private val repository = UsersRepository()
    private val usersLiveData = MutableLiveData<List<Person>>()
    private val toastLiveData = MutableLiveData<String>()
    private val isLoadingLiveData = MutableLiveData<Boolean>()

    val users: LiveData<List<Person>>
        get() = usersLiveData

    val toast: LiveData<String>
        get() = toastLiveData

    val isLoading: LiveData<Boolean>
        get() = isLoadingLiveData

    fun getUsers() {
        isLoadingLiveData.postValue(true)
        repository.getUsers()
            .subscribe({ persons ->
                isLoadingLiveData.postValue(false)
                usersLiveData.postValue(persons)
                saveUsersToDb(persons)
            },
                { error ->
                    isLoadingLiveData.postValue(false)
                    toastLiveData.postValue(error.message)
                    getUsersFromDb()
                })

    }

    private fun saveUsersToDb(list: List<Person>) {
        repository.saveUsersToDb(list)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({},
                {
                    toastLiveData.postValue(it.message.orEmpty())
                })


    }

    private fun getUsersFromDb() {
        repository.getUsersFromDb()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ persons ->
                usersLiveData.postValue(persons)
            }, { error ->
                toastLiveData.postValue(error.message.orEmpty())
            })
    }


}