package com.example.proyectoappgym.remote

import android.annotation.SuppressLint
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.entity.data.TokenResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var token: TokenResponse? = sessionManager.tokenResponse.value
        var request: Request? = null
        var expiredAt: Long? = runBlocking { sessionManager.getExpiredAt() }
        var finalToken: TokenResponse? = null

        sessionManager.verifyToken(token, expiredAt)
        finalToken = sessionManager.tokenResponse.value
        request = chain.request().newBuilder().apply {
            if(!finalToken?.accessToken.isNullOrBlank()) addHeader("Authorization", "Bearer $token")
        }.build()

        return chain.proceed(request)
    }

    /*Preguntar dos veces por el token antes y despues de entrar en el synchronized,
      permite que dos hilos no modifiquen el token dos veces y de error
      cuando el segundo hilo va a ejecutar*//*
    fun verifyToken(tokenResponse: TokenResponse?, expiredAt: Long?) {
        val newToken: TokenResponse?
        val newExpiredAt: Long?

        if (tokenResponse != null && expiredAt != null)
            synchronized(this) {
                newToken = sessionManager.tokenResponse.value
                newExpiredAt = runBlocking { sessionManager.getExpiredAt() }
                if (isTokenExpired(tokenResponse.expiresIn, expiredAt))
                    runBlocking {
                        sessionManager.refreshToken(tokenResponse.refreshToken)
                    }
                else throw IllegalArgumentException("Error of session")
            }
    }

    private fun isTokenExpired(expiresIn: Int, expiredAt: Long): Boolean {
        return System.currentTimeMillis() + expiresIn >= expiredAt - 60
    }*/

}