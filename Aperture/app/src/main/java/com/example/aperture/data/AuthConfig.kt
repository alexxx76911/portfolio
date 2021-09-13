package com.example.aperture.data

import net.openid.appauth.ResponseTypeValues

object AuthConfig {
    const val AUTH_URI = "https://unsplash.com/oauth/authorize"
    const val TOKEN_URI = "https://unsplash.com/oauth/token"
    const val RESPONSE_TYPE = ResponseTypeValues.CODE
    const val SCOPE = "public read_user read_photos write_likes read_collections"

    const val CLIENT_ID = "TEzHp_GV2tnxF_sWwElsgoeCI3KPpeauL9EofdXzRHE"
    const val CLIENT_SECRET = "UG0-Hf_Hbb_NksCqvBFPYjqARrAzLcdTujSt3furc2c"
    const val CALLBACK_URL = "aperture://aperture.ru/callback"

}