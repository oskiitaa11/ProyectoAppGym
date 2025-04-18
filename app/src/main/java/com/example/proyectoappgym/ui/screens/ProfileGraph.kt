package com.example.proyectoappgym.ui.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object ProfileGraphRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<ProfileGraphRoute>(startDestination = ProfileRoute) {
        profileDestination({ navController.goToEditProfileScreen() }, {  } )
        editProfileDestination({ navController.popBackStack() })
    }
}