package com.example.bazar.model.net

import com.example.bazar.model.repository.TokenInMemory
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthChecker : Authenticator, KoinComponent {
    private val api by inject<ApiService>()
    override fun authenticate(route: Route?, response: Response): Request? {

        if (TokenInMemory.token != null) {

        }

    }

    private fun refreshToken() {
        api.refreshToken()
    }
}