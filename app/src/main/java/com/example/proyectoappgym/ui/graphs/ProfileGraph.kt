package com.example.proyectoappgym.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.proyectoappgym.ui.screens.ProfileRoute
import com.example.proyectoappgym.ui.screens.editProfileDestination
import com.example.proyectoappgym.ui.screens.goToEditProfileScreen
import com.example.proyectoappgym.ui.screens.goToQuestionForModifier
import com.example.proyectoappgym.ui.screens.profileDestination
import com.example.proyectoappgym.ui.screens.questionForModifierDestination
import kotlinx.serialization.Serializable

@Serializable
object ProfileGraphRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<ProfileGraphRoute>(startDestination = ProfileRoute) {
        profileDestination(
            { navController.goToEditProfileScreen() },
            {  }
        )
        editProfileDestination({ navController.popBackStack() }, { questionForModifier, selectedResponses, responseType -> navController.goToQuestionForModifier(questionForModifier, selectedResponses, responseType) })
        questionForModifierDestination { navController.popBackStack() }
    }
}