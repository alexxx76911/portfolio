package com.skillbox.github.ui.current_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skillbox.github.User

class CurrentUserViewModel : ViewModel() {

    private val userLiveData = MutableLiveData<User?>()
    private val errorLiveData = MutableLiveData<Throwable>()

    val userData: LiveData<User?>
        get() = userLiveData

    val errorData: LiveData<Throwable>
        get() = errorLiveData

    private val repository = CurrentUserRepository()


    fun getUserInfo() {
        repository.getInfo({ user ->
            userLiveData.postValue(user)
        }, { error ->
            errorLiveData.postValue(error)

        })


    }
}