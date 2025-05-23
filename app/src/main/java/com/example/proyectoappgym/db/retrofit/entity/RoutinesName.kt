package com.example.proyectoappgym.db.retrofit.entity

import com.example.proyectoappgym.entity.TrainingRoutine
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RoutinesName(
    @SerializedName("routines")
    @Expose
    val trainingRoutines: List<TrainingRoutine>
)