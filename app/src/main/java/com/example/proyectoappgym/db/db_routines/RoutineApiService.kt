package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.http.GET
import retrofit2.http.POST

interface RoutineApiService {
    @GET("/api/routines")
    fun getAllRoutines(): MutableStateFlow<List<TrainingRoutine>>

    @POST("/api/routines/update-routine")
    fun updateRoutine(newTrainingRoutines: List<TrainingRoutine>)
}