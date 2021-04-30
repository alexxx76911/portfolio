package com.skillbox.github

import retrofit2.Call
import retrofit2.http.*

interface GitHubApi {

    @GET("user")
    fun getUserInfo(
        @Header("accept") accept: String = "application/vnd.github.v3+json"
    ): Call<User>


    @GET("repositories")
    fun getRepos(
        @Header("accept") accept: String = "application/vnd.github.v3+json"
    ): Call<List<Repository>>

    @GET("user/starred/{owner}/{repo}")
    fun getStarInfo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("accept") accept: String = "application/vnd.github.v3+json"
    ): Call<Unit>

    @PUT("user/starred/{owner}/{repo}")
    fun putStarToRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("accept") accept: String = "application/vnd.github.v3+json"

    ): Call<Unit>

    @DELETE("user/starred/{owner}/{repo}")
    fun removeStarForRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Header("accept") accept: String = "application/vnd.github.v3+json"

    ): Call<Unit>
}

