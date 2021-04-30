package com.skillbox.github.ui.repository_details

import android.util.Log

import com.skillbox.github.data.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException

class RepoDetailRepository {


    fun getStarInfo(
        owner: String,
        repo: String,
        callback: (isStarred: Boolean) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        Network.gitHubApi.getStarInfo(owner, repo).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                callback(response.code() == 204)
                Log.d("response", response.code().toString())
                Log.d("response", response.message())
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {

                onError(t)
            }
        })
    }


    fun putStarToRepo(owner: String, repo: String, onError: (Throwable) -> Unit) {
        Network.gitHubApi.putStarToRepo(owner, repo).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    onError(RuntimeException("incorrect response code"))
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onError(t)

            }

        })
    }


    fun removeStarForRepo(owner: String, repo: String, onError: (Throwable) -> Unit) {
        Network.gitHubApi.removeStarForRepo(owner, repo).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    onError(RuntimeException("incorrect response code"))
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onError(t)

            }

        })
    }


}