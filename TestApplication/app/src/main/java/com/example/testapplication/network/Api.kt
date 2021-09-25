package com.example.testapplication.network

import com.example.testapplication.Data
import retrofit2.Call
import retrofit2.http.GET


interface Api {
    @GET("users")
    fun getUsers(): Call<Data>

}