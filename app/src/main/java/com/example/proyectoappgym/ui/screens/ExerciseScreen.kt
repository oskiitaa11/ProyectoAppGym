package com.example.proyectoappgym.ui.screens

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
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

@Serializable
data class ExerciseRoute(val exerciseString: String)

fun NavController.goToExerciseRoute(exercise: RealizationExercise) {
    val convertToJson = Gson()
    val stringExercise = convertToJson.toJson(exercise)

    navigate(ExerciseRoute(stringExercise))
}

@RequiresApi(Build.VERSION_CODES.P)
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

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ExerciseScreen(realizationExercise: RealizationExercise, goBackToHome: () -> Unit) {
    val context = LocalContext.current
    val state = rememberScrollState()
    val uri = "android.resource://${context.packageName}/raw/peck_deck_machine".toUri()
    /*val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                add(ImageDecoderDecoder.Factory()) // Para Android 9+ (API 28+)
            }
            .build()
    }*/


    Scaffold(
        topBar = { ShowTopAppBarExerciseScreen(realizationExercise.exercise.name, goBackToHome) }
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

            /*AsyncImage(
                model = ImageRequest.Builder(context)
                    .data("file:///android_asset/chest_press_machine.gif")
                    .crossfade(true)
                    .build(),
                contentDescription = "Gift exercise",
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth(0.8f).height(200.dp).align(Alignment.CenterHorizontally)
            )*/

             // si está en res/raw

            VideoPlayer(uri)
            ListItem(
                headlineContent = { Text("How to do it?", fontStyle = FontStyle.Italic, modifier = Modifier.padding(bottom = 5.dp)) },
                leadingContent = { Icon(painter = painterResource(R.drawable.ic_ordinal_list), contentDescription = "Steps icon", tint = Color.White) },
                supportingContent = { Text(realizationExercise.exercise.stepsForDoIt) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent, headlineColor = Color.White, leadingIconColor = Color.White, supportingColor = Color.White),
            )

            Text("Type of exercise", fontStyle = FontStyle.Italic, fontSize = 15.sp, color = Color.White, modifier = Modifier.padding(start = 55.dp))
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 55.dp)) {
                Image(painter = painterResource(realizationExercise.exercise.type.idIcon), contentDescription = "Icon type", modifier = Modifier.size(64.dp).padding(end = 10.dp))
                Text(realizationExercise.exercise.type.nameType)
            }
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
                useController = true // o false si no quieres controles
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}