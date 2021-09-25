package com.example.testapplication

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val data: List<Person>
)
