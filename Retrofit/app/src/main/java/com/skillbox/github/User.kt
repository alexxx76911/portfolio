package com.skillbox.github

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val login: String,
    @Json(name = "avatar_url")
    val avatar: String,
    @Json(name = "public_repos")
    val reposNumber: Int?
)
