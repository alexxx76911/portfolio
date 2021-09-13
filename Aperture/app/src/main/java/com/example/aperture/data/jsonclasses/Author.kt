package com.example.aperture.data.jsonclasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Author(
        val id: String,
        val username: String,
        val name: String?,
        val profile_image: ProfileImage?,
        val bio: String?
)
