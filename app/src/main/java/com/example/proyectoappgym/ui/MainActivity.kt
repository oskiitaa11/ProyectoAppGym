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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.proyectoappgym.entity.Gender
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.theme.ProyectoAppGymTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute
@Serializable
object RegistrationRoute
@Serializable
data class RegistrationQuestionsRoute(val name: String, val username: String, val password: String, val email: String, val birthdate: String, val gender: Gender)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isSignIn by remember { mutableStateOf(false) }
            val changeSignIn: (Boolean) -> Unit = { isSignIn = it }

            ProyectoAppGymTheme {
                if(isSignIn) {
                    //NavScreensWithingLoginScreen()
                } else {
                    NavScreensWithLoginScreen(changeSignIn)
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
fun NavScreensWithLoginScreen(signIn: (Boolean) -> Unit) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = Modifier.fillMaxSize()
    ) {
        composable<LoginRoute> { navBackStackEntry ->
            val loginViewmodel: LoginViewmodel = viewModel(navBackStackEntry) {
                LoginViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
                )
            }
            val intCompletedSignIn by loginViewmodel.intCompletedSignIn.collectAsStateWithLifecycle()
            val context = LocalContext.current

            LoginScreen(
                { navController.navigate(RegistrationRoute) },
                { username, password ->
                    loginViewmodel.signIn(username, password)
                    if(intCompletedSignIn == 1) {
                        signIn(true)
                        showToast("Sesion started", context)
                    }
                },
                intCompletedSignIn
            )
        }

        composable<RegistrationRoute> { navBacStackEntry ->
            val registrationViewmodel: RegistrationViewmodel = viewModel(navBacStackEntry) {
                RegistrationViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
                )
            }

            RegistrationScreen(
                { navController.popBackStack() },
                { name, username, password, email, birthdate, gender -> navController.navigate(RegistrationQuestionsRoute(name, username, password, email, birthdate, gender)) },
                { username -> registrationViewmodel.userExist(username) }
            )
        }

        composable<RegistrationQuestionsRoute> { navBackStackEntry ->
            val registrationQuestionsRoute: RegistrationQuestionsRoute = navBackStackEntry.toRoute()
            val registrationQuestionsViewmodel: RegistrationQuestionsViewmodel = viewModel {
                RegistrationQuestionsViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).repositoryQuestions,
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
                )
            }
            val allQuestions = registrationQuestionsViewmodel.allQuestions
            var user : User
            with(registrationQuestionsRoute) {
                user = User(username, password, email, name, birthdate, gender)
            }

            RegistrationQuestionsScreen(
                user,
                allQuestions,
                { userForAdd -> registrationQuestionsViewmodel.addUser(userForAdd) },
                { navController.popBackStack(LoginRoute, true) }
            )
        }
    }

}

/*
@Composable
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
}
*/

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