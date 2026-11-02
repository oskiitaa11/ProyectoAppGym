package com.example.proyectoappgym.ui.graphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.proyectoappgym.ui.screens.ErrorRoute
import com.example.proyectoappgym.ui.screens.ProfileRoute
import com.example.proyectoappgym.ui.screens.editProfileDestination
import com.example.proyectoappgym.ui.screens.goToEditProfileScreen
import com.example.proyectoappgym.ui.screens.goToErrorDestination
import com.example.proyectoappgym.ui.screens.goToQuestionForModifier
import com.example.proyectoappgym.ui.screens.goToUpdateEmailProfileScreen
import com.example.proyectoappgym.ui.screens.goToUpdatePasswordProfileScreen
import com.example.proyectoappgym.ui.screens.profileDestination
import com.example.proyectoappgym.ui.screens.questionForModifierDestination
import com.example.proyectoappgym.ui.screens.updateEmailProfileDestination
import com.example.proyectoappgym.ui.screens.updatePasswordProfileDestination
import kotlinx.serialization.Serializable

@Serializable
object ProfileGraphRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<ProfileGraphRoute>(startDestination = ProfileRoute) {
        profileDestination(
            { navController.goToEditProfileScreen() },
            { navController.goToUpdateEmailProfileScreen() },
            { navController.goToUpdatePasswordProfileScreen() },
            { message, idIcon -> navController.navigate(ErrorRoute(message, idIcon)) }
        )
        editProfileDestination(
            { navController.popBackStack() },
            { questionForModifier, responseType-> navController.goToQuestionForModifier(questionForModifier, responseType) },
            { message, idIcon -> navController.goToErrorDestination(message, idIcon) }
        )
        questionForModifierDestination({ navController.popBackStack() }, { message, idIcon -> navController.navigate(
            ErrorRoute(message, idIcon)) })
        updateEmailProfileDestination { navController.popBackStack() }
        updatePasswordProfileDestination { navController.popBackStack() }
    }
}