package com.example.proyectoappgym.entity

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
data class TrainingRoutine(
    @SerializedName("dayOfWeek")
    @Expose
    val dayOfWeek: DayOfWeek,
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("exercises")
    @Expose
    var exercises: List<RealizationExercise>
) {
    constructor(): this(DayOfWeek.MONDAY, "", emptyList())
}