package com.example.proyectoappgym.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.goToHomeScreen(){
    navigate(HomeRoute)
}

fun NavGraphBuilder.homeDestination() {
    composable<HomeRoute> {
        HomeScreen()
    }
}

@Composable
fun HomeScreen() {

}