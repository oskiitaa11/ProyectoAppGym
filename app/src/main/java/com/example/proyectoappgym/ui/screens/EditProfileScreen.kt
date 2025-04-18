package com.example.proyectoappgym.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import kotlinx.serialization.Serializable

@Serializable
object EditProfileRoute

fun NavController.goToEditProfileScreen() {
    navigate(EditProfileRoute)
}

fun NavGraphBuilder.editProfileDestination(backProfileScreen: () -> Unit) {
    composable<EditProfileRoute> { navBackStackEntry ->
        val editProfileViewmodel: EditProfileViewmodel = viewModel(navBackStackEntry) {
            EditProfileViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
            )
        }
        val currentUser by editProfileViewmodel.currentUser.collectAsStateWithLifecycle()

        EditProfileScreen(currentUser, backProfileScreen)
    }
}

@Composable
fun EditProfileScreen(currentUser: User, backProfileScreen: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(colorResource(R.color.lightBlack))
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(currentUser.profileAvatar),
            contentDescription = "Profile avatar",
            modifier = Modifier.border(width = 3.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
                .height(80.dp)
                .width(80.dp)
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(horizontalArrangement = Arrangement.Center) {
            Text(currentUser.username, color = Color.White)

        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}