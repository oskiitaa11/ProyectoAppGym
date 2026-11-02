package com.example.proyectoappgym

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.proyectoappgym.entity.users.Gender
import com.example.proyectoappgym.entity.BottomBarItem
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.ui.SessionState
import com.example.proyectoappgym.ui.graphs.HomeGraphRoute
import com.example.proyectoappgym.ui.screens.HomeRoute
import com.example.proyectoappgym.ui.screens.LoginScreen
import com.example.proyectoappgym.ui.graphs.ProfileGraphRoute
import com.example.proyectoappgym.ui.graphs.homeGraph
import com.example.proyectoappgym.ui.viewmodels.LoginViewmodel
import com.example.proyectoappgym.ui.screens.ProfileRoute
import com.example.proyectoappgym.ui.screens.goToHomeScreen
import com.example.proyectoappgym.ui.screens.goToProfileScreen
import com.example.proyectoappgym.ui.graphs.profileGraph
import com.example.proyectoappgym.ui.graphs.registrationGraph
import com.example.proyectoappgym.ui.screens.ErrorScreen
import com.example.proyectoappgym.ui.theme.ProyectoAppGymTheme
import com.example.proyectoappgym.ui.viewmodels.SessionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
object SplashScreen
@Serializable
object LoginRoute
@Serializable
object RegistrationRoute
@Serializable
data class RegistrationQuestionsRoute(val name: String, val username: String, val password: String, val email: String, val birthdate: String, val gender: Gender)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        enableEdgeToEdge()
        setContent {
            ProyectoAppGymTheme {
                val sessionViewmodel: SessionViewmodel = hiltViewModel()
                val stateUser by sessionViewmodel.sessionState.collectAsStateWithLifecycle(
                    SessionState.Loading)
                val showAnimation by sessionViewmodel.showAnimation.collectAsStateWithLifecycle()

                ShowScreensWithAnimation(stateUser, showAnimation)
            }
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavScreensWithingLoginScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBottomBar = currentDestination?.hierarchy?.any { it.hasRoute<ProfileRoute>() || it.hasRoute<HomeRoute>() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if(showBottomBar == true)
                BottomBar(currentDestination, navController)
        }
    ) { innerpadding ->
        NavHost(
            navController = navController,
            startDestination = HomeGraphRoute,
            popExitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
            popEnterTransition = { slideInHorizontally(animationSpec = tween(800)) },
            enterTransition = { slideInHorizontally(animationSpec = tween(800)) },
            exitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerpadding.calculateBottomPadding())
        ) {

            homeGraph(navController)
            profileGraph(navController)
        }
    }

}


@SuppressLint("UnusedCrossfadeTargetStateParameter", "UnusedMaterial3ScaffoldPaddingParameter",
    "FlowOperatorInvokedInComposition", "UnrememberedMutableState", "WrongStartDestinationType"
)
@Composable
fun NavScreensWithLoginScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = Modifier.fillMaxSize(),
        popExitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(800)) },
        enterTransition = { slideInHorizontally(animationSpec = tween(800)) },
        exitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
    ) {

        composable<LoginRoute> { navBackStackEntry ->
            val loginViewmodel: LoginViewmodel = hiltViewModel()
            val errorToSignIn: ErrorForShowing? by loginViewmodel.errorToSignIn.collectAsStateWithLifecycle(null)

            LoginScreen(
                errorToSignIn,
                { loginViewmodel.updateErrorToNullToSignIn() },
                { navController.navigate(RegistrationRoute) },
                { username, password ->
                    loginViewmodel.signIn(username, password)
                }
            )
        }

        registrationGraph(navController)
    }
}

@Composable
fun BottomBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val items = listOf(
        BottomBarItem(
            title = "Home",
            iconSelectedBottom = ImageVector.vectorResource(R.drawable.ic_home_24),
            iconBottom = ImageVector.vectorResource(R.drawable.ic_outline_home_24),
            selected = currentDestination?.hierarchy?.any { it.hasRoute<HomeRoute>() } == true,
            onClick = { navController.goToHomeScreen() }
        ),

        BottomBarItem(
            title = "Profile",
            iconSelectedBottom = ImageVector.vectorResource(R.drawable.ic_person_24),
            iconBottom = ImageVector.vectorResource(R.drawable.ic_person_outline_24),
            selected = currentDestination?.hierarchy?.any { it.hasRoute<ProfileGraphRoute>() } == true,
            onClick = { navController.goToProfileScreen() }
        ),
    )

    Column {
        HorizontalDivider(color = Color.White, thickness = 2.dp)

        NavigationBar(
            containerColor = colorResource(R.color.lightBlack)
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    label = { Text(item.title) },
                    icon = { Icon(imageVector = if(item.selected) item.iconSelectedBottom else item.iconBottom, contentDescription = "Go to Screen" )  },
                    selected = item.selected,
                    onClick = item.onClick,
                    colors = NavigationBarItemColors(Color.White, Color.White, Color.Transparent, Color.White, Color.White, Color.Gray, Color.Gray),
                )
            }
        }
    }

}

@Composable
fun ShowScreensWithAnimation(stateUser: SessionState, showAnimation: Boolean) {
    AnimatedContent(
        targetState = stateUser,
        transitionSpec = {
            if (showAnimation) {
                slideInHorizontally { width -> width } + fadeIn() togetherWith
                        slideOutHorizontally { width -> -width } + fadeOut()
            } else {
                EnterTransition.None togetherWith ExitTransition.None
            }
        }
    ) { state ->
        when(state) {
            SessionState.Loading -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = colorResource(R.color.lightBlack)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) { CircularProgressIndicator(color = colorResource(R.color.lightGreen)) }
            SessionState.Authenticated -> NavScreensWithingLoginScreen()
            SessionState.Unauthenticated -> NavScreensWithLoginScreen()
            is SessionState.Error -> ErrorScreenAtTheBeginning(state.error, state.idIcon)
        }
    }
}

@Composable
fun ErrorScreenAtTheBeginning(error: String, idIcon: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ErrorScreen(error, idIcon)
    }
}



@Composable
fun GetLightGreen(): Color {
    return colorResource(R.color.lightGreen)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoAppGymTheme {

    }
}