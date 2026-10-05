package com.example.proyectoappgym.db.db_auth

import com.example.proyectoappgym.entity.data.TokenResponse
import com.example.proyectoappgym.entity.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/sign-up")
    public fun signUp(user: User)

    @POST("/api/auth/sign-in")
    public fun signIn( authUser: Map<String, Any>): MutableStateFlow<TokenResponse>

    @POST("/api/logout")
    public fun logout()

    @DELETE("/api/auth/delete")
    public fun deleteCurrentUser()

    @POST("/api/auth/refresh-token")
    public fun refreshToken(refreshToken: String): MutableStateFlow<TokenResponse>
}