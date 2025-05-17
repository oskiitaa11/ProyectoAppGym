package com.example.proyectoappgym.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.ui.viewmodels.ExerciseViewmodel
import kotlinx.serialization.Serializable

@Serializable
data class ExerciseRoute(val exercise: Exercise)

fun NavGraphBuilder.exerciseDestination() {
    composable<ExerciseRoute> { navBackStackEntry ->
        val exerciseViewmodel: ExerciseViewmodel = viewModel(navBackStackEntry) {
            ExerciseViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
            )
        }
    }
}

@Composable
fun ExerciseScreen(exercise: Exercise) {

}