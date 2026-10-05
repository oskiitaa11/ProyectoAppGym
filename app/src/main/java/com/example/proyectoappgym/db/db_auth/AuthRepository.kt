package com.example.proyectoappgym.db.db_auth

import android.annotation.SuppressLint
import com.example.proyectoappgym.entity.data.TokenResponse
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.remote.RetrofitClient
import com.example.proyectoappgym.remote.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService, private val sessionManager: SessionManager) {

    suspend fun signUp(user: User) {
        authApiService.signUp(user)
    }

    suspend fun signIn(authUser: Map<String, Any>): MutableStateFlow<TokenResponse> {
        var tokenResponse: TokenResponse? = null

        authApiService.signIn(authUser).collect { tokenResponse = it }
        if (tokenResponse != null)
            sessionManager.saveAccessToken(tokenResponse)
        else throw IllegalArgumentException("Request rejected")
    }

    suspend fun logout() {
        authApiService.logout()
        sessionManager.clearSession()
    }

    suspend fun deleteCurrentUser() {
        sessionManager.clearSession()
        authApiService.deleteCurrentUser()
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun refreshToken(refreshToken: String): MutableStateFlow<TokenResponse> {
        var tokenResponse: TokenResponse? = null

        sessionManager.saveAccessToken(tokenResponse)
        else throw IllegalArgumentException("There has been a problem with user session")
    }
}