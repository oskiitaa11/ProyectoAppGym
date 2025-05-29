package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
import com.example.proyectoappgym.entity.RealizationExercise
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.ui.viewmodels.ExerciseViewmodel
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import java.nio.file.WatchEvent
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.proyectoappgym.entity.Muscles

@Serializable
data class ExerciseRoute(val exerciseString: String)

fun NavController.goToExerciseRoute(exercise: RealizationExercise) {
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
        val exercise: RealizationExercise = json.fromJson(exerciseRoute.exerciseString, RealizationExercise::class.java)

        ExerciseScreen(exercise, goBackToHome)
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun ExerciseScreen(realizationExercise: RealizationExercise, goBackToHome: () -> Unit) {
    val state = rememberScrollState()
    val context = LocalContext.current
    val uri = "android.resource://${context.packageName}/raw/${realizationExercise.exercise.exercise.nameVideo}".toUri()

    Scaffold(
        topBar = { ShowTopAppBarExerciseScreen(realizationExercise.exercise.exercise.name, goBackToHome) }
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
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lvl: ", color = Color.White)
                Image(
                    painter = painterResource(realizationExercise.exercise.exercise.exerciseLevel.idIconLvl),
                    contentDescription = "Icon Lvl",
                    modifier = Modifier.size(30.dp).padding(bottom = 8.dp)
                )
            }

            VideoPlayer(uri)
            ListItem(
                headlineContent = { Text("How to do it?", fontStyle = FontStyle.Italic, modifier = Modifier.padding(bottom = 5.dp)) },
                leadingContent = { Icon(painter = painterResource(R.drawable.ic_ordinal_list), contentDescription = "Steps icon", tint = Color.White) },
                supportingContent = { Text(realizationExercise.exercise.exercise.stepsForDoIt) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent, headlineColor = Color.White, leadingIconColor = Color.White, supportingColor = Color.White),
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text("Type of exercise", fontStyle = FontStyle.Italic, fontSize = 15.sp, color = Color.White, modifier = Modifier.padding(start = 55.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 55.dp)) {
                Image(painter = painterResource(realizationExercise.exercise.exercise.type.idIcon), contentDescription = "Icon type", modifier = Modifier.size(64.dp).padding(end = 10.dp))
                Text(realizationExercise.exercise.exercise.type.nameType, color = Color.White)
            }
            Spacer(modifier = Modifier.height(20.dp))
            ShowInvolvedMuscles(realizationExercise.exercise.exercise.idImageMuscles, realizationExercise.exercise.exercise.trainedPrimaryMuscles, realizationExercise.exercise.exercise.trainedSecondaryMuscles)
            Spacer(modifier = Modifier.height(20.dp))
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
                Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Icon back", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.lightBlack), titleContentColor = Color.White)
    )
}

@SuppressLint("DiscouragedApi")
@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = true
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                Lifecycle.Event.ON_DESTROY -> exoPlayer.release()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = false // o false si no quieres controles
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@Composable
fun ShowInvolvedMuscles(idImageMuscles: Int, primaryMuscles: List<Muscles>, secondaryMuscles: List<Muscles>) {
    val primaryMusclesString = primaryMuscles.map { it.nameMuscle }.toString().replace("[", "").replace("]", "")
    val secondaryMusclesString = secondaryMuscles.map { it.nameMuscle }.toString().replace("[", "").replace("]", "")

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)) {
        Image(painter = painterResource(R.drawable.icon_muscles), contentDescription = "Icon Muscles", modifier = Modifier.size(25.dp))
        Text("Involved muscles", color = Color.White, fontStyle = FontStyle.Italic, modifier = Modifier.padding(start = 15.dp))
    }
    Row {
        Image(painter = painterResource(idImageMuscles), contentDescription = "Muscles Image", modifier = Modifier.padding(start = 40.dp).size(200.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(" ", modifier = Modifier.background(Color(224, 98, 75)).size(7.dp))
                Text("Primary muscles:", fontSize = 8.sp, lineHeight = 12.sp, modifier = Modifier.padding(start = 5.dp), color = Color.White)
            }
            Text(primaryMusclesString, fontSize = 8.sp, lineHeight = 12.sp, color = Color.White)

            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(" ", modifier = Modifier.background(Color(215, 159, 143)).size(7.dp))
                Text("Secondary muscles:", fontSize = 8.sp, lineHeight = 12.sp, modifier = Modifier.padding(start = 5.dp), color = Color.White)
            }
            Text(secondaryMusclesString, fontSize = 8.sp, lineHeight = 12.sp, color = Color.White)
        }
    }

}