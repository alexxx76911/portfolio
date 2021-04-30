package com.skillbox.github


import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Repository(
    val id: Long,
    val name: String,
    val owner: User
)
