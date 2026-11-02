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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CreateRestoreCredentialRequest
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.InitializerViewModelFactoryBuilder
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.users.Avatars
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import kotlinx.serialization.Serializable

@Serializable
object ProfileRoute

fun NavController.goToProfileScreen() {
    navigate(ProfileRoute)
}

fun NavGraphBuilder.profileDestination(onEditProfileScreen: () -> Unit, onUpdateEmailProfile: () -> Unit, onUpdatePasswordProfile: () -> Unit, onErrorScreen: (String, Int) -> Unit) {
    composable<ProfileRoute>(
        enterTransition = { fadeIn(animationSpec = tween(800)) },
        exitTransition = { fadeOut(animationSpec = tween(800)) }
    ) { navBackStackEntry ->
        val profileViewmodel: ProfileViewmodel = hiltViewModel()
        val currentUser by profileViewmodel.currentUser.collectAsStateWithLifecycle()
        val error by profileViewmodel.error.collectAsStateWithLifecycle(null)

        LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
            profileViewmodel.reloadUser()
        }

        LaunchedEffect(error) {
            if (error != null && error!!.errorType == ErrorType.ERROR_IN_ERROR_SCREEN) onErrorScreen(
                error!!.error, error?.idIcon as Int
            )
        }

        if(currentUser.username.isNotEmpty()) {
            ProfileScreen(
                currentUser,
                error,
                onEditProfileScreen,
                onUpdateEmailProfile,
                onUpdatePasswordProfile,
                { profileViewmodel.logout() },
                { profileViewmodel.deleteCurrentUser() }
            )
        } else
            LoadingScreen()
    }
}

@Composable
fun ProfileScreen(user: User, errorWhileUpdating: ErrorForShowing?, onEditProfileScreen: () -> Unit, onUpdateEmailProfile: () -> Unit, onUpdatePasswordProfile: () -> Unit, logout: () -> Unit, deleteUser: () -> Unit) {
    val resourceAvatar = Avatars.toAvatar(user.idProfileAvatar)
    var errorDialog by rememberSaveable { mutableStateOf(errorWhileUpdating) }
    val state = rememberScrollState()

    errorDialog?.let { ShowErrorDialogForLogging(it.error, it.idIcon as Int, { errorDialog = null }) }

    Scaffold(topBar = { ShowTopAppBarProfile() }) { innerpadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White)
                .padding(innerpadding)
                .fillMaxSize().verticalScroll(state)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(resourceAvatar.idAvatarImage),
                contentDescription = "Profile avatar",
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        color = colorResource(R.color.lightGreen),
                        shape = CircleShape
                    )
                    .height(80.dp)
                    .width(80.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(user.name, color = colorResource(R.color.lightBlack))
            Spacer(modifier = Modifier.height(15.dp))
            TextButton(
                onClick = onEditProfileScreen,
                shape = ShapeDefaults.Medium,
                colors = ButtonColors(
                    colorResource(R.color.lightBlack),
                    Color.White,
                    Color.DarkGray,
                    Color.DarkGray
                )
            ) {
                Text("Edit Profile")
            }
            Spacer(modifier = Modifier.height(50.dp))
            HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.lightBlack))
            ShowItemWithDialog(
                text = "Email",
                colorText = colorResource(R.color.lightBlack),
                showDialog = false,
                leadingIdIcon = R.drawable.ic_email_64,
                onClickConfirmButton = { onUpdateEmailProfile() },
            )
            HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.lightBlack))
            ShowItemWithDialog(
                text = "Password",
                colorText = colorResource(R.color.lightBlack),
                leadingIdIcon = R.drawable.ic_lock_unfilled_64,
                showDialog = false,
                onClickConfirmButton = { onUpdatePasswordProfile() },
            )
            HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.lightBlack))
            ShowItemWithDialog(
                text = "Log out",
                colorText = colorResource(R.color.red),
                textForDialog = "Are you sure you want to log out?",
                titleForDialog = "Log out the user",
                leadingIdIcon = R.drawable.ic_logout,
                showDialog = true,
                onClickConfirmButton = logout,
            )
            HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.lightBlack))
            ShowItemWithDialog(
                text = "Delete user",
                colorText = colorResource(R.color.red),
                background = colorResource(R.color.light_error),
                textForDialog = "Are you sure delete current user?",
                titleForDialog = "Delete current user",
                leadingIdIcon = R.drawable.ic_trash,
                showDialog = true,
                onClickConfirmButton = deleteUser,
            )
            HorizontalDivider(thickness = 2.dp, color = colorResource(R.color.lightBlack))
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