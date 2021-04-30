package com.example.homework.network


import com.example.homework.SearchResult
import retrofit2.http.GET
import retrofit2.http.Query


interface MoviesApi {

    @GET("/")
    suspend fun getMovies(
        @Query("apikey") key: String = Network.API_KEY,
        @Query("s") title: String,
        @Query("type") type: String
    ): SearchResult
}