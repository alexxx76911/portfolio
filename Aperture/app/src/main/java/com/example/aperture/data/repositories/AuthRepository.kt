package com.example.aperture.data.repositories

import android.content.Context
import android.net.Uri

import androidx.core.content.edit
import com.example.aperture.data.AccessToken
import com.example.aperture.data.AuthConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.*

class AuthRepository(context: Context) {
    private val sharedReferences = context.getSharedPreferences(TOKEN_SAVE, Context.MODE_PRIVATE)

    fun getAuthRequest(): AuthorizationRequest {
        val serviceConfiguration = AuthorizationServiceConfiguration(
                Uri.parse(AuthConfig.AUTH_URI),
                Uri.parse(AuthConfig.TOKEN_URI)
        )

        val redirectUri = Uri.parse(AuthConfig.CALLBACK_URL)

        return AuthorizationRequest.Builder(
                serviceConfiguration,
                AuthConfig.CLIENT_ID,
                AuthConfig.RESPONSE_TYPE,
                redirectUri
        )
                .setScope(AuthConfig.SCOPE)
                .build()
    }

    fun performTokenRequest(
            authService: AuthorizationService,
            tokenRequest: TokenRequest,
            onComplete: () -> Unit,
            onError: () -> Unit
    ) {
        authService.performTokenRequest(tokenRequest, getClientAuthentication()) { response, ex ->
            when {
                response != null -> {
                    val accessToken = response.accessToken.orEmpty()
                    AccessToken.token = "Bearer $accessToken"
                    onComplete()
                }
                else -> {
                    onError()
                }
            }
        }
    }

    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }

    suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            sharedReferences.edit { putString(TOKEN_KEY, token) }
        }
    }

    suspend fun getSavedToken(): String? {
        return withContext(Dispatchers.IO) {
            sharedReferences.getString(TOKEN_KEY, null)
        }
    }

    suspend fun logOut() {
        AccessToken.token = ""
        withContext(Dispatchers.IO) {
            sharedReferences.edit {
                remove(TOKEN_KEY)
            }
        }
    }


    companion object {
        const val TOKEN_SAVE = "tokenSave"
        const val TOKEN_KEY = "tokenKey"
    }
}