package com.example.aperture.data.jsonclasses


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotoWrapper(
        val photo: Photo
)
