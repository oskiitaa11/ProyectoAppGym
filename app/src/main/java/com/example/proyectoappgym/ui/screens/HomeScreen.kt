package com.example.proyectoappgym.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.HomeViewmodel
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.goToHomeScreen(){
    navigate(HomeRoute)
}

fun NavGraphBuilder.homeDestination() {

    composable<HomeRoute> { navBackStackEntry ->
        val homeViewmodel: HomeViewmodel = viewModel(navBackStackEntry) {
            HomeViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).uidLoggedUser as String
            )
        }
        val currentUser by homeViewmodel.currentUser.collectAsStateWithLifecycle()

        HomeScreen(currentUser)
    }
}

@Composable
fun HomeScreen(user: User) {

}