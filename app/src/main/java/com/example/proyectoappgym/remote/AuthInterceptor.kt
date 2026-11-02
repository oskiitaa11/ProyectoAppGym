package com.example.proyectoappgym.remote

import com.example.proyectoappgym.entity.data.request.TokenResponse
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val sessionManager: SessionManager): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val finalToken = runBlocking {
            sessionManager.getValidToken()
        }
        val request = chain.request().newBuilder().apply {
            finalToken?.accessToken
                ?.takeIf { it.isNotBlank() }
                ?.let { addHeader("Authorization", "Bearer $it") }
        }.build()

        return chain.proceed(request)
    }

}