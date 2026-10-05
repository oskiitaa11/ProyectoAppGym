package com.example.proyectoappgym.remote

import android.annotation.SuppressLint
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.entity.data.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager, private val authRepository: AuthRepository): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionManager.getCurrentToken()
        val request = chain.request().newBuilder().apply {
            if(!token.isNullOrBlank()) addHeader("Authorization", "Bearer $token")
        }.build()

        return chain.proceed(request)
    }

    suspend fun verifyToken(refreshToken: String) {
        val expiresIn = sessionManager.getExpiresIn() ?: throw IllegalArgumentException("There has been a problem with user session")
        val expiredAt = sessionManager.getExpiredAt() ?: throw IllegalArgumentException("There has been a problem with user session")
        val refreshToken = sessionManager.getRefreshToken() ?: throw IllegalArgumentException("There has been a problem with user session")

        if (System.currentTimeMillis() >= expiredAt - 60)
            refreshToken(refreshToken)
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun refreshToken(refreshToken: String): MutableStateFlow<TokenResponse> {
        var tokenResponse: TokenResponse? = null

        authRepository.refreshToken(refreshToken).collect { tokenResponse = it }
        if (tokenResponse != null) {
            sessionManager.saveAccessToken(tokenResponse)
        } else {
            throw IllegalArgumentException("There has been a problem with user session")
        }

    }

}