package com.example.proyectoappgym.db.db_users

import com.example.proyectoappgym.entity.data.request.DataUserRequest
import com.example.proyectoappgym.entity.data.request.RefreshTokenRequest
import com.example.proyectoappgym.entity.data.request.TokenResponse
import com.example.proyectoappgym.entity.data.request.UpdateEmailRequest
import com.example.proyectoappgym.entity.data.request.UpdatePasswordRequest
import com.example.proyectoappgym.entity.users.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserApiService {
    @PUT("/api/users/me")
    suspend fun updateCurrentUser(@Body dataUserRequest: DataUserRequest): Response<User>

    @GET("/api/users/me")
    suspend fun getCurrentUser(): Response<User>

    @POST("/api/logout")
    suspend fun logout(): Response<Unit>

    @DELETE("/api/delete")
    suspend fun deleteCurrentUser(): Response<Unit>

    @PUT("/api/update-pw")
    suspend fun updatePassword(@Body newPassword: UpdatePasswordRequest): Response<Unit>

    @PUT("/api/update-email")
    suspend fun updateEmail(@Body newEmail: UpdateEmailRequest): Response<Unit>
}