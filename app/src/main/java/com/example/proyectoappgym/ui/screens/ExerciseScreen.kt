package com.example.proyectoappgym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.DayOfWeek
import com.example.proyectoappgym.entity.Exercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.ui.viewmodels.ExerciseViewmodel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import java.nio.file.WatchEvent

@Serializable
data class ExerciseRoute(val exerciseString: String)

fun NavController.goToExerciseRoute(exercise: Exercise) {
    val convertToJson = Gson()
    val stringExercise = convertToJson.toJson(exercise)

    navigate(ExerciseRoute(stringExercise))
}

fun NavGraphBuilder.exerciseDestination(goBackToHome: () -> Unit) {
    composable<ExerciseRoute> { navBackStackEntry ->
        val exerciseViewmodel: ExerciseViewmodel = viewModel(navBackStackEntry) {
            ExerciseViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
            )
        }
        val json = Gson()
        val exerciseRoute: ExerciseRoute = navBackStackEntry.toRoute()
        val exercise: Exercise = json.fromJson(exerciseRoute.exerciseString, Exercise::class.java)

        ExerciseScreen(exercise, goBackToHome)
    }
}

@Composable
fun ExerciseScreen(exercise: Exercise, goBackToHome: () -> Unit) {
    val state = rememberScrollState()

    Scaffold(
        topBar = { ShowTopAppBarExerciseScreen(exercise.name, goBackToHome) }
    ) { innerpadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxSize().padding(
                top = innerpadding.calculateTopPadding(),
                end = innerpadding.calculateRightPadding(LayoutDirection.Rtl),
                start = innerpadding.calculateLeftPadding(LayoutDirection.Ltr)
            ).background(colorResource(R.color.lightBlack)).verticalScroll(state)
        ) {
            ListItem(
                headlineContent = { Text("How to do it?", fontStyle = FontStyle.Italic) },
                leadingContent = { Icon(painter = painterResource(R.drawable.ic_ordinal_list), contentDescription = "Steps icon", tint = Color.White) },
                supportingContent = { Text(exercise.stepsForDoIt) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent, headlineColor = Color.White, leadingIconColor = Color.White, supportingColor = Color.White),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarExerciseScreen(nameExercise: String, goBackToHome: () -> Unit) {
    TopAppBar(
        title = { Text(nameExercise, fontSize = 15.sp) },
        navigationIcon = {
            IconButton(goBackToHome) {
                Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Icon back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.lightBlack), titleContentColor = Color.White)
    )
}