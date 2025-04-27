package com.example.proyectoappgym.ui.screens

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.example.proyectoappgym.App
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import kotlinx.serialization.Serializable

@Serializable
object ProfileGraphRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
    navigation<ProfileGraphRoute>(startDestination = ProfileRoute) {
        profileDestination({ navController.goToEditProfileScreen() }, {  })
        editProfileDestination { navController.popBackStack() }
    }
}