package com.example.proyectoappgym.ui.graphs

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.proyectoappgym.LoginRoute
import com.example.proyectoappgym.RegistrationQuestionsRoute
import com.example.proyectoappgym.RegistrationRoute
import com.example.proyectoappgym.ui.screens.registrationDestination
import com.example.proyectoappgym.ui.screens.registrationQuestionsDestination
import com.example.proyectoappgym.ui.viewmodels.SharedRegistrationViewmodel
import kotlinx.serialization.Serializable

@Serializable
object RegistrationGraphRoute

fun NavGraphBuilder.registrationGraph(navController: NavController) {
    navigation<RegistrationGraphRoute>(startDestination = RegistrationRoute) {
        registrationDestination(navController)
        registrationQuestionsDestination(navController)
    }
}