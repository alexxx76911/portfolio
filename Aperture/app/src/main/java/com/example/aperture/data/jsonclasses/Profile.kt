package com.example.aperture.data.jsonclasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Profile(
        val id: String,
        val username: String,
        val first_name: String,
        val last_name: String,
        val location: Location?,
        val total_likes: Int,
        val total_photos: Int,
        val total_collections: Int,
        val downloads: Int,
        val email: String,
        val links: ProfileLinks,
        val bio: String?
)
