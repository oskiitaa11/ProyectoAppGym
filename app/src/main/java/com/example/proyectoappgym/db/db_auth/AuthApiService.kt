package com.example.proyectoappgym.db.db_auth

import com.example.proyectoappgym.entity.data.TokenResponse
import com.example.proyectoappgym.entity.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/sign-up")
    fun signUp(user: User)

    @POST("/api/auth/sign-in")
    fun signIn( authUser: Map<String, Any>): MutableStateFlow<TokenResponse>

    @POST("/api/logout")
    fun logout()

    @DELETE("/api/auth/delete")
    fun deleteCurrentUser()

    @POST("/api/auth/refresh-token")
    fun refreshToken(refreshToken: String): MutableStateFlow<TokenResponse>
}