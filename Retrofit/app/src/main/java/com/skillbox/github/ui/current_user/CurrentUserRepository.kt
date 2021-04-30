package com.skillbox.github.ui.current_user

import com.skillbox.github.User
import com.skillbox.github.data.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.lang.RuntimeException

class CurrentUserRepository {

    fun getInfo(
        callback: (user: User?) -> Unit,
        onError: (Throwable) -> Unit
    ) {

        Network.gitHubApi.getUserInfo().enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    onError(RuntimeException("incorrect response code"))
                }

            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                onError(t)

            }

        })

    }
}