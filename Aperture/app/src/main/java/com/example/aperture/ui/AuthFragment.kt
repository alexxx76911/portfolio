package com.example.aperture.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.aperture.R
import com.example.aperture.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse
import net.openid.appauth.GrantTypeValues
import net.openid.appauth.TokenRequest

class AuthFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: AuthViewModel by viewModels()
    private val binding: FragmentLoginBinding by viewBinding()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        Glide.with(requireView())
                .load(R.raw.unsplash_logo_small)
                .into(binding.logoImageView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTH_REQUEST_CODE && data != null) {
            val response = AuthorizationResponse.fromIntent(data)
            var tokenExchangeRequest: TokenRequest? = null
            if (response != null) {
                tokenExchangeRequest = TokenRequest.Builder(
                        response.request.configuration,
                        response.request.clientId
                )
                        .setGrantType(GrantTypeValues.AUTHORIZATION_CODE)
                        .setRedirectUri(response.request.redirectUri)
                        .setCodeVerifier(null)
                        .setAuthorizationCode(response.authorizationCode)
                        .setAdditionalParameters(null)
                        .setNonce(response.request.nonce)
                        .build()
            }
            val exception = AuthorizationException.fromIntent(data)
            when {
                tokenExchangeRequest != null && exception == null ->
                    viewModel.onAuthCodeReceived(tokenExchangeRequest)
                exception != null -> viewModel.onAuthCodeFailed(exception)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun bindViewModel() {
        binding.loginBtn.setOnClickListener { viewModel.openLoginPage() }
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::updateIsLoading)
        viewModel.openAuthPageLiveData.observe(viewLifecycleOwner, ::openAuthPage)
        viewModel.snackbar.observe(viewLifecycleOwner, Observer { message ->
            Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).apply {
                view.background.alpha = 70
                show()
            }
        })
        viewModel.authSuccessLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(StartFragmentDirections.actionStartFragmentToMainFragment())
        }
    }


    private fun updateIsLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading

    }

    private fun openAuthPage(intent: Intent) {
        startActivityForResult(intent, AUTH_REQUEST_CODE)
    }

    companion object {
        private const val AUTH_REQUEST_CODE = 342
    }

}