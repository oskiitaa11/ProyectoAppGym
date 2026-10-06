package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class RoutineRepository @Inject constructor(private val routineApiService: RoutineApiService) {
    //private val apiServiceRoutine = RetrofitClient.routineApi

    suspend fun getAllRoutines(): MutableStateFlow<List<TrainingRoutine>> {
        return routineApiService.getAllRoutines()
    }

    suspend fun updateRoutine(trainingRoutines: List<TrainingRoutine>) {
        routineApiService.updateRoutine(trainingRoutines)
    }
}