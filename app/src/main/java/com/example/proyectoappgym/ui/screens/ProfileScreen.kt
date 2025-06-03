package com.example.proyectoappgym.ui.screens

import android.R.attr.contentDescription
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CreateRestoreCredentialRequest
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

fun NavController.goToProfileScreen() {
    navigate(ProfileRoute)
}

fun NavGraphBuilder.profileDestination(onEditProfileScreen: () -> Unit, onSettingsScreen: () -> Unit) {
    composable<ProfileRoute>(
        enterTransition = { fadeIn(animationSpec = tween(800)) },
        exitTransition = { fadeOut(animationSpec = tween(800)) }
    ) { navBackStackEntry ->
        val profileViewmodel: ProfileViewmodel = viewModel(navBackStackEntry) {
            ProfileViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
            )
        }
        val currentUser by profileViewmodel.currentUser.collectAsStateWithLifecycle()

        if(currentUser.username.isNotEmpty())
            ProfileScreen(currentUser, onEditProfileScreen, onSettingsScreen) { profileViewmodel.createRoutine() }
    }
}

@Composable
fun ProfileScreen(user: User, onEditProfileScreen: () -> Unit, onSettingsScreen: () -> Unit, createRoutine: () -> Unit) {
    Scaffold(topBar = { ShowTopAppBarProfile() }) { innerpadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(colorResource(R.color.lightBlack)).padding(innerpadding).fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(user.profileAvatar),
                contentDescription = "Profile avatar",
                modifier = Modifier.border(width = 3.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
                    .height(80.dp)
                    .width(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(user.name, color = Color.White)
            Spacer(modifier = Modifier.height(15.dp))
            TextButton(
                onClick = onEditProfileScreen,
                shape = ShapeDefaults.Medium,
                colors = ButtonColors(Color(255f, 255f, 255f, 0.2f), Color.White, Color.DarkGray, Color.DarkGray)
            ) {
                Text("Edit Profile")
            }
            TextButton(createRoutine) {
                Text("Create")
            }
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarProfile() {
    Column {
        TopAppBar(
            title = { Text("Account") },
            colors = TopAppBarColors(colorResource(R.color.lightBlack), Color.DarkGray, Color.Gray, Color.White, Color.DarkGray)
        )
        HorizontalDivider(color = Color.White, thickness = 2.dp)
    }
}