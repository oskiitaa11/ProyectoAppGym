package com.example.proyectoappgym.db.db_users

import com.example.proyectoappgym.entity.users.Avatars
import com.example.proyectoappgym.entity.data.DataUserRequest
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.example.proyectoappgym.entity.users.User
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserApiService {
    @PUT("/api/users/me")
    suspend fun updateCurrentUser(dataUserRequest: DataUserRequest): MutableStateFlow<User>

    @GET("/api/users/me")
    suspend fun getCurrentUser(): MutableStateFlow<User?>
}