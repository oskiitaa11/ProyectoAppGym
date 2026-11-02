package com.example.proyectoappgym.db.db_auth

import com.example.proyectoappgym.entity.data.request.FullUserRequest
import com.example.proyectoappgym.entity.data.request.RefreshTokenRequest
import com.example.proyectoappgym.entity.data.request.RequestExistingUsername
import com.example.proyectoappgym.entity.data.request.ResponseExistingUser
import com.example.proyectoappgym.entity.data.request.SignInUser
import com.example.proyectoappgym.entity.data.request.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApiService {
    @POST("/api/auth/sign-up")
    suspend fun signUp(@Body fullUserRequest: FullUserRequest): Response<Unit>

    @POST("/api/auth/sign-in")
    suspend fun signIn(@Body authUser: SignInUser): Response<TokenResponse>

    @POST("/api/auth/refresh-token")
    suspend fun refreshToken(@Body refreshToken: RefreshTokenRequest): Response<TokenResponse>

    @POST("/api/users/asking")
    suspend fun existsUser(@Body requestExistingUsername: RequestExistingUsername): Response<ResponseExistingUser>

    @GET("/api/users/asking")
    suspend fun existsEmailUser(@Query("email") email: String): Map<String, Any>
}