package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.data.request.TrainingRoutineRequest
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RoutineApiService {
    @GET("/api/routines/me")
    suspend fun getAllRoutines(): Response<List<TrainingRoutineRequest>>

    @POST("/api/routines/update-routine")
    suspend fun updateRoutine(@Body newTrainingRoutines: List<TrainingRoutineRequest>): Response<Unit>
}