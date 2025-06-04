package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.media.Image
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.createBitmap
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
import com.example.proyectoappgym.entity.ExerciseLevel
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.RealizationExercise
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

fun NavGraphBuilder.homeDestination(onExerciseScreen: (RealizationExercise) -> Unit, onProfileScreen: () -> Unit) {

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
            HomeScreen(currentUser, onProfileScreen, onExerciseScreen)
    }
}


@Composable
fun HomeScreen(user: User, onProfileScreen: () -> Unit, onExerciseScreen: (RealizationExercise) -> Unit) {
    val state = rememberLazyListState()
    //Se asigna la siguiente rutina de entrenamiento, es decir la que tiene un numero mayor que el numero del dia actual
    var nextTrainingRoutine: TrainingRoutine = getNextTrainingRoutine(user.trainingRoutines)

    Scaffold(
        topBar = { ShowTopAppBarHome(user.name, user.profileAvatar.idAvatar, onProfileScreen) },
    ) { innerpadding ->
        Column(modifier = Modifier.fillMaxSize().padding(
            top = innerpadding.calculateTopPadding(),
            end = innerpadding.calculateRightPadding(LayoutDirection.Rtl),
            start = innerpadding.calculateLeftPadding(LayoutDirection.Ltr)
        ).background(colorResource(R.color.lightBlack)))  {
            Text("Your next training:", color = Color.White, fontStyle = FontStyle.Italic, modifier = Modifier.padding(start = 10.dp))
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.White)
            LazyColumn(state = state) {
                itemsIndexed(items = nextTrainingRoutine.exercises) { _, realizationExercise ->
                    ShowExercise(realizationExercise, onExerciseScreen)
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableInteractionSource")
@Composable
fun ShowExercise(realizationExercise: RealizationExercise, onExerciseScreen: (RealizationExercise) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(realizationExercise.exercise.exercise.name, fontSize = 12.sp) },
        leadingContent = { Image(painter = painterResource(realizationExercise.exercise.exercise.idCoverImage), contentDescription = "Cover Image", contentScale = ContentScale.Crop, modifier = Modifier.size(80.dp)) },
        overlineContent = { Image(painter = painterResource(realizationExercise.exercise.exercise.exerciseLevel.idIconLvl), contentDescription = "Lvl exercise", modifier = Modifier.size(30.dp).padding(bottom = 10.dp)) },
        supportingContent = { Text("${realizationExercise.series} sets of ${realizationExercise.repetitions} repetitions") },
        modifier = Modifier.fillMaxWidth().clickable(interactionSource = interactionSource, indication = LocalIndication.current) { onExerciseScreen(realizationExercise) },
        colors = ListItemDefaults.colors(containerColor = colorResource(R.color.lightBlack), headlineColor = Color.White, overlineColor = Color.White.copy(alpha = 0.7f), supportingColor = Color.White.copy(alpha = 0.5f))
    )

    HorizontalDivider(thickness = 2.dp, color = Color.White)
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

fun getNextTrainingRoutine(trainingRoutines: List<TrainingRoutine>): TrainingRoutine {
    val actualDay = LocalDate.now().dayOfWeek.value
    val bestIdDay = trainingRoutines.maxOf { it.dayOfWeek.idDay }
    //Si no hay rutina siguiente en la semana, se empieza por la primera que encuentre en la semana siguiente y sino se busca la primera con el id mayor que el dia actual
    var nextTrainingRoutine: TrainingRoutine = (if(actualDay > bestIdDay) trainingRoutines.find { actualDay >= it.dayOfWeek.idDay }
    else if(actualDay == bestIdDay) trainingRoutines.find { actualDay == it.dayOfWeek.idDay }
    else trainingRoutines.find { actualDay <= it.dayOfWeek.idDay }) ?: TrainingRoutine()

    return nextTrainingRoutine
}