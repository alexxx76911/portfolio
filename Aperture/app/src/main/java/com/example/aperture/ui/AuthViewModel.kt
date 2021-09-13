package com.example.aperture.ui

import android.app.Application
import android.content.Intent

import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aperture.R
import com.example.aperture.data.AccessToken
import com.example.aperture.data.repositories.AuthRepository
import com.example.aperture.util.SingleLiveEvent
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationService
import net.openid.appauth.TokenRequest

class AuthViewModel(private val app: Application) : AndroidViewModel(app) {
    private val authRepository = AuthRepository(app)
    private val authService: AuthorizationService = AuthorizationService(getApplication())
    private val openAuthPageLiveEvent = SingleLiveEvent<Intent>()
    private val snackbarLiveData = SingleLiveEvent<String>()
    private val loadingMutableLiveData = MutableLiveData(false)
    private val authSuccessLiveEvent = SingleLiveEvent<Unit>()

    val openAuthPageLiveData: LiveData<Intent>
        get() = openAuthPageLiveEvent

    val loadingLiveData: LiveData<Boolean>
        get() = loadingMutableLiveData

    val snackbar: LiveData<String>
        get() = snackbarLiveData

    val authSuccessLiveData: LiveData<Unit>
        get() = authSuccessLiveEvent

    fun onAuthCodeFailed(exception: AuthorizationException) {
        loadingMutableLiveData.postValue(false)
        showMessage(app.getString(R.string.auth_failed))

    }

    fun onAuthCodeReceived(tokenRequest: TokenRequest) {
        loadingMutableLiveData.postValue(true)
        authRepository.performTokenRequest(
                authService = authService,
                tokenRequest = tokenRequest,
                onComplete = {
                    loadingMutableLiveData.postValue(false)
                    authSuccessLiveEvent.postValue(Unit)
                    saveToken()
                },
                onError = {
                    loadingMutableLiveData.postValue(false)
                    showMessage(app.getString(R.string.auth_failed))

                }
        )
    }

    fun openLoginPage() {
        loadingMutableLiveData.postValue(true)
        val customTabsIntent = CustomTabsIntent.Builder()
                .build()

        val openAuthPageIntent = authService.getAuthorizationRequestIntent(
                authRepository.getAuthRequest(),
                customTabsIntent
        )

        openAuthPageLiveEvent.postValue(openAuthPageIntent)
    }

    private fun saveToken() {
        viewModelScope.launch {
            authRepository.saveToken(AccessToken.token)
        }
    }

    override fun onCleared() {
        super.onCleared()
        authService.dispose()
    }

    private fun showMessage(message: String) {
        snackbarLiveData.postValue(message)
    }


}