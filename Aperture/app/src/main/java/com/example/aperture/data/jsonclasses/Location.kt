package com.example.aperture.data.jsonclasses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
        val city: String?,
        val country: String?,
        val position: Position?
)
