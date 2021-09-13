package com.example.aperture.network

import com.example.aperture.data.jsonclasses.*
import okhttp3.ResponseBody
import retrofit2.http.*

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(@Query("page") page: Int): List<Photo>

    @GET("users/{userName}/photos")
    suspend fun getUserPhotos(@Path("userName") userName: String, @Query("page") page: Int): List<Photo>

    @GET("users/{userName}/likes")
    suspend fun getUserLikedPhotos(@Path("userName") userName: String, @Query("page") page: Int): List<Photo>

    @GET("collections")
    suspend fun getCollections(@Query("page") page: Int): List<PhotoCollection>

    @GET("users/{userName}/collections")
    suspend fun getUserCollections(@Path("userName") userName: String, @Query("page") page: Int): List<PhotoCollection>

    @GET("collections/{id}/photos")
    suspend fun getCollectionPhotos(@Path("id") id: String, @Query("page") page: Int): List<Photo>

    @GET("photos/{id}")
    suspend fun getPhotoById(@Path("id") id: String): Photo

    @GET("collections/{id}")
    suspend fun getCollectionById(@Path("id") id: String): PhotoCollection

    @GET("search/photos")
    suspend fun searchPhotos(@Query("query") query: String): SearchResult

    @POST("photos/{id}/like")
    suspend fun likePhoto(@Path("id") id: String): PhotoWrapper

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(@Path("id") id: String): PhotoWrapper

    @GET("me")
    suspend fun getProfile(): Profile

    @GET
    suspend fun getProfileByUrl(@Url url: String): Author

    @GET("photos/{id}/download")
    suspend fun trackDownload(@Path("id") id: String)


    @GET
    suspend fun downLoadImage(@Url url: String): ResponseBody
}