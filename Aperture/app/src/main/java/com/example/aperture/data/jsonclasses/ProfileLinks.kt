package com.example.aperture.data.jsonclasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileLinks(
        val self: String,
        val photos: String,
        val likes: String
)
