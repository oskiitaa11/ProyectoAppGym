package com.example.proyectoappgym.db.db_routines

import com.example.proyectoappgym.entity.data.request.TrainingRoutineRequest
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorResponseBackend
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response

import javax.inject.Inject

class RoutineRepository @Inject constructor(private val routineApiService: RoutineApiService) {

    suspend fun getAllRoutines(): List<TrainingRoutine> {
        val response = routineApiService.getAllRoutines()
        val errorResponseBackend: String
        val trainingRoutineFromRequest: List<TrainingRoutine>

        if (!response.isSuccessful || response.body().isNullOrEmpty()) {
            errorResponseBackend = response.errorBody()?.string() ?: ""
            throw ErrorException.fromJson(errorResponseBackend,
                ErrorException("routines_not_found", "The routines haven't found, please check your routines in your profile"))
        }
        trainingRoutineFromRequest = response.body()!!.map { TrainingRoutine.getTrainingRoutineFromResponse(it) }

        return trainingRoutineFromRequest
    }

    suspend fun updateRoutine(trainingRoutines: List<TrainingRoutine>) {
        val trainingRoutinesRequest = trainingRoutines.map { TrainingRoutineRequest.getTrainingRoutineRequestFromRoutine(it) }
        val response = routineApiService.updateRoutine(trainingRoutinesRequest)
        val errorResponseBackend: String

        if (!response.isSuccessful) {
            errorResponseBackend = response.errorBody()?.string() ?: ""
            throw ErrorException.fromJson(errorResponseBackend,
                ErrorException("not_update_routine", "There has been while the routine updated"))
        }
    }
}