package com.example.proyectoappgym.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.theme.ProyectoAppGymTheme
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute
@Serializable
object RegistrationRoute
@Serializable
data class RegistrationQuestionsRoute(val name: String, val username: String, val password: String, val email: String, val birthdate: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProyectoAppGymTheme {
                if(true) {
                    NavScreensWithLoginScreen()
                } else {
                    //NavScreensWithingLoginScreen()
                }

            }
        }
    }
}

/*@Composable
fun NavScreensWithingLoginScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = { BottomBar(currentDestination, navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginGraphRoute,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            loginGraph(navController)
            profileDestination()
        }
    }

}*/


@Composable
fun NavScreensWithLoginScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<LoginRoute> { navBackStackEntry ->
            LoginScreen({ navController.navigate(RegistrationRoute) })
        }

        composable<RegistrationRoute> {
            RegistrationScreen({ navController.popBackStack() }, { name, username, password, email, birthdate -> navController.navigate(RegistrationQuestionsRoute(name, username, password, email, birthdate)) })
        }

        composable<RegistrationQuestionsRoute> { navBackStackEntry ->
            val registrationQuestionsRoute: RegistrationQuestionsRoute = navBackStackEntry.toRoute()
            val registrationQuestionsViewmodel: RegistrationQuestionsViewmodel = viewModel {
                RegistrationQuestionsViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).repositoryQuestions
                )
            }
            val allQuestions = registrationQuestionsViewmodel.allQuestions
            var user : User
            with(registrationQuestionsRoute) {
                user = User(name, username, password, email, birthdate)
            }

            RegistrationQuestionsScreen(user, allQuestions)
        }
    }

}

/*@Composable
fun BottomBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val items = listOf(
        BottomBarItem(
            title = "Home",
            icon = Icons.Default.Home,
            selected = currentDestination?.hierarchy?.any { it.hasRoute<HomeRoute>() } == true,
            onClick = { navController.goToHomeScreen() }
        ),

        BottomBarItem(
            title = "Profile",
            icon = Icons.Default.AccountCircle,
            selected = currentDestination?.hierarchy?.any { it.hasRoute<ProfileRoute>() } == true,
            onClick = { navController.goToProfileScreen() }
        ),
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                label = { Text(item.title) },
                icon = { Icon(imageVector = item.icon, contentDescription = "Go to Screen" ) },
                selected = item.selected,
                onClick = item.onClick
            )

        }
    }
}*/

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