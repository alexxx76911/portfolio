package com.example.homework

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    val Search: List<Movie>?
)
