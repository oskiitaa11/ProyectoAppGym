package com.example.proyectoappgym.ui.screens

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.SettingsViewmodel
import kotlinx.serialization.Serializable

@Serializable
object SettingsRoute

fun NavController.goToSettingsScreen() {
    navigate(SettingsRoute)
}

fun NavGraphBuilder.settingsDestination(backEditProfileScreen: () -> Unit) {
    composable<SettingsRoute> { navBackStackEntry ->
        val settingsViewmodel: SettingsViewmodel = hiltViewModel()
    }
}