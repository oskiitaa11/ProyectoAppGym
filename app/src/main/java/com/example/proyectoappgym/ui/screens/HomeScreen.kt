package com.example.proyectoappgym.ui.screens

import android.media.Image
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.TrainingRoutine
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

fun NavGraphBuilder.homeDestination(navController: NavController) {

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
            HomeScreen(currentUser, { navController.navigate(ProfileRoute) }) { exercise -> navController.navigate(ExerciseRoute(exercise)) }
    }
}

@Composable
fun HomeScreen(user: User, onProfileScreen: () -> Unit, onExerciseScreen: (Exercise) -> Unit) {
    var state = rememberLazyListState()
    var actualDay = LocalDate.now().dayOfWeek.value
    var trainingRoutineToday = user.trainingRoutines.find { actualDay >= it.dayOfWeek.idDay } ?: TrainingRoutine()

    Scaffold(
        topBar = { ShowTopAppBarHome(user.name, user.profileAvatar, onProfileScreen) }
    ) { innerpadding ->
        Column(modifier = Modifier.padding(innerpadding).fillMaxSize().background(colorResource(R.color.lightBlack)))  {
            Text("Your next training:", color = Color.White, fontStyle = FontStyle.Italic, modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.White)
            LazyColumn(state = state) {
                itemsIndexed(items = trainingRoutineToday.exercises) { _, exercise ->
                    ShowExercise(exercise, onExerciseScreen)
                }
            }
        }
    }
}

@Composable
fun ShowExercise(exercise: Exercise, onExerciseScreen: (Exercise) -> Unit) {
    ListItem(
        headlineContent = { Text(exercise.name) },
        leadingContent = {  },
        supportingContent = { Text("${exercise.series} sets of ${exercise.repetitions} repetitions") },
        modifier = Modifier.fillMaxWidth(),
        colors = ListItemDefaults.colors(containerColor = colorResource(R.color.dark_blue), headlineColor = Color.White, overlineColor = Color.White.copy(alpha = 0.7f), supportingColor = Color.White.copy(alpha = 0.5f))
    )
    HorizontalDivider(color = Color.White.copy(alpha = 0.5f))

    /*Card(
        { onExerciseScreen(exercise) },
        colors = CardDefaults.cardColors(containerColor = Color(255,255,255), contentColor = colorResource(R.color.dark_blue)),
        shape = ShapeDefaults.Medium
    ) {
        Text(exercise.name)
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarHome(name: String, idAvatar: Int, onProfileScreen: () -> Unit) {
    TopAppBar(
        title = {
            Text("Welcome $name!", fontSize = 18.sp, modifier = Modifier.padding(start = 5.dp))
        },
        navigationIcon = {
            IconButton(onProfileScreen) {
                Image(
                    painter = painterResource(idAvatar),
                    contentDescription = "Profile avatar",
                    modifier = Modifier.border(width = 1.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
                        .height(35.dp)
                        .width(35.dp)
                        .clip(CircleShape)
                        .padding(top = 1.dp)
                )
            }

        },
        colors = TopAppBarColors(colorResource(R.color.lightBlack), Color.DarkGray, Color.Gray, Color.White, Color.DarkGray)
    )
}