package com.skillbox.github.ui.repository_list

import com.skillbox.github.Repository
import com.skillbox.github.data.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class ReposListRepository {

    fun getRepos(callback: (List<Repository>?) -> Unit, onError: (Throwable) -> Unit) {
        Network.gitHubApi.getRepos().enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    onError(RuntimeException("incorrect response code"))
                }
            }

            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                onError(t)

            }

        })

    }
}