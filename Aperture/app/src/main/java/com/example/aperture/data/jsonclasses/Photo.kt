package com.example.aperture.data.jsonclasses


import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Photo(
        val id: String,
        val width: Int,
        val height: Int,
        val user: Author,
        val liked_by_user: Boolean,
        val likes: Int,
        val urls: Urls,
        val exif: Exif?,
        val location: Location?,
        val tags: List<Tag>?,
        val downloads: Int?,
        //val links: Links
)

