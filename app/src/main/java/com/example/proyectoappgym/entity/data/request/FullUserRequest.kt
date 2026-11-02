package com.example.proyectoappgym.entity.data.request

import com.example.proyectoappgym.entity.functions.getParsedBirthdate
import com.example.proyectoappgym.entity.users.User
import com.google.gson.annotations.SerializedName

data class FullUserRequest(
    @SerializedName("user")
    val userRequest: UserRequest,
    @SerializedName("routines")
    val trainingRoutines: List<TrainingRoutineRequest>
) {
    companion object {
        fun getFullUserRequestFromUser(user: User): FullUserRequest {
            val trainingRoutineRequest = user.trainingRoutines.map { TrainingRoutineRequest.getTrainingRoutineRequestFromRoutine(it) }

            return FullUserRequest(
                UserRequest(user.email, user.password,
                    DataUserRequest(user.username, user.name, getParsedBirthdate(user.birthdate), user.gender, user.allQuestionsAnswered, user.idProfileAvatar)
                ), trainingRoutineRequest
            )
        }
    }
    

}