package com.example.aperture.data.jsonclasses


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoCollection(
        val id: String,
        val title: String,
        val total_photos: Int,
        val cover_photo: Photo,
        val user: Author,
        val description: String?
)
