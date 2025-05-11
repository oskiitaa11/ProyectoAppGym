package com.example.proyectoappgym.ui.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.HomeViewmodel
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.goToHomeScreen(){
    navigate(HomeRoute)
}

fun NavGraphBuilder.homeDestination() {

    composable<HomeRoute>(
        enterTransition = { fadeIn(animationSpec = tween(800)) },
        exitTransition = { fadeOut(animationSpec = tween(800)) }
    ) { navBackStackEntry ->
        val homeViewmodel: HomeViewmodel = viewModel(navBackStackEntry) {
            HomeViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
            )
        }
        val currentUser by homeViewmodel.currentUser.collectAsStateWithLifecycle()
        val routines by homeViewmodel.stringRoutines.collectAsStateWithLifecycle()

        if(currentUser.username.isNotEmpty())
            HomeScreen(currentUser, routines) { questions ->
                homeViewmodel.getRoutines(questions)
            }
    }
}

@Composable
fun HomeScreen(user: User, routines: String, getRoutines: (Map<String, List<String>>) -> Unit) {
    var c = "hola"

    Button({ c = "holita" }) {
        Text(c)
    }

    routines
}