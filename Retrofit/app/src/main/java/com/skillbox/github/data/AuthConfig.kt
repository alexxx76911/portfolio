package com.skillbox.github.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {

    const val AUTH_URI = "https://github.com/login/oauth/authorize"
    const val TOKEN_URI = "https://github.com/login/oauth/access_token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "user,repo"

    const val CLIENT_ID = "e088f43b56d7edc4397f"
    const val CLIENT_SECRET = "3384dbda7178d4b09e841ad64c7fc0f9550af99a"
    const val CALLBACK_URL = "skillbox://skillbox.ru/callback"

}