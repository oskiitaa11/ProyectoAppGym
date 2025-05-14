package com.example.proyectoappgym.ui.screens

import android.media.Image
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.HomeViewmodel
import kotlinx.serialization.Serializable
import java.time.LocalDate
import kotlin.contracts.contract

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

        if(currentUser.username.isNotEmpty())
            HomeScreen(currentUser)
    }
}

@Composable
fun HomeScreen(user: User) {
    var state = rememberLazyListState()
    var actualDay = LocalDate.now().dayOfWeek.toString().lowercase()
    var trainingRoutineActualDay = user.trainingRoutines.find { actualDay == it.dayOfWeek.toString().lowercase() }
    Scaffold(
        topBar = { ShowTopAppBarHome(user.name, user.profileAvatar) }
    ) { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding).fillMaxSize())  {
            Text("Dayli routine", color = Color.White, fontStyle = FontStyle.Italic)
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.White)
            LazyColumn(state = state) {
                itemsIndexed(items = user.trainingRoutines) { _, trainingRoutine ->

                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarHome(name: String, idAvatar: Int) {
    TopAppBar(
        title = {
            Text("Welcome $name!", modifier = Modifier.padding(start = 10.dp))
        },
        navigationIcon = { Icon(painter = painterResource(idAvatar), contentDescription = "Avatar user") },
        colors = TopAppBarColors(colorResource(R.color.lightBlack), Color.DarkGray, Color.Gray, Color.White, Color.DarkGray)
    )
}