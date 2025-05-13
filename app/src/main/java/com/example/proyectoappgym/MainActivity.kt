package com.example.proyectoappgym

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.proyectoappgym.RegistrationRoute
import com.example.proyectoappgym.entity.Gender
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.entity.BottomBarItem
import com.example.proyectoappgym.ui.screens.HomeRoute
import com.example.proyectoappgym.ui.screens.LoginScreen
import com.example.proyectoappgym.ui.screens.ProfileGraphRoute
import com.example.proyectoappgym.ui.viewmodels.LoginViewmodel
import com.example.proyectoappgym.ui.screens.ProfileRoute
import com.example.proyectoappgym.ui.screens.RegistrationQuestionsScreen
import com.example.proyectoappgym.ui.viewmodels.RegistrationQuestionsViewmodel
import com.example.proyectoappgym.ui.screens.RegistrationScreen
import com.example.proyectoappgym.ui.screens.ShowTopAppBarEditProfileScreen
import com.example.proyectoappgym.ui.screens.ShowTopAppBarProfile
import com.example.proyectoappgym.ui.viewmodels.RegistrationViewmodel
import com.example.proyectoappgym.ui.screens.goToHomeScreen
import com.example.proyectoappgym.ui.screens.goToProfileScreen
import com.example.proyectoappgym.ui.screens.homeDestination
import com.example.proyectoappgym.ui.screens.profileDestination
import com.example.proyectoappgym.ui.screens.profileGraph
import com.example.proyectoappgym.ui.theme.ProyectoAppGymTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute
@Serializable
object RegistrationRoute
@Serializable
data class RegistrationQuestionsRoute(val name: String, val username: String, val password: String, val email: String, val birthdate: String, val gender: Gender)

class MainActivity : ComponentActivity() {


    private lateinit var launcher: ((String) -> Unit) -> ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcher = { authWithGoogle ->
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    authWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    Log.w("TAG", "Google sign in failed", e)
                }
            }
        }

        enableEdgeToEdge()
        setContent {
            val contextMainAct = LocalContext.current
            val app = contextMainAct.applicationContext as App
            val thereIsLoggedUser by app.isLoggedUser.collectAsStateWithLifecycle(null)
            val reassignUser = {
                if(app.userDatabase.getUidLoggedUser() != null)
                    app.addLoggedUserFromMain(app.userDatabase.getUidLoggedUser()!!)
            }
            /*val signInGoogle: @Composable (Context, (String) -> Unit) -> Unit = { context, authWithGoogle ->
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(
                        ContextCompat.getString(
                            context,
                            R.string.default_web_client_id
                        )
                    )
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, gso)
                val sigInIntent = googleSignInClient.signInIntent
                launcher(authWithGoogle).launch(sigInIntent)
            }*/

            ProyectoAppGymTheme {
                if(thereIsLoggedUser != null)
                if(thereIsLoggedUser as Boolean) {
                    NavScreensWithingLoginScreen()
                } else {
                    NavScreensWithLoginScreen(reassignUser)
                }

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
            if(showBottomBar ?: false)
                BottomBar(currentDestination, navController)
        }
    ) { innerpadding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            popExitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
            popEnterTransition = { slideInHorizontally(animationSpec = tween(800)) },
            enterTransition = { slideInHorizontally(animationSpec = tween(800)) },
            exitTransition = { slideOutHorizontally(animationSpec = tween(800)) },
            modifier = Modifier
                .fillMaxSize().padding(bottom = innerpadding.calculateBottomPadding())
        ) {
            homeDestination()
            profileGraph(navController)
        }
    }

}


@SuppressLint("UnusedCrossfadeTargetStateParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavScreensWithLoginScreen(reassignLoggedUser: () -> Unit/*launcher: (Context, (String) -> Unit) -> Unit*/) {
    val navController = rememberNavController()
    //val enterTransition: EnterTransition = fadeIn(initialAlpha = 1f, animationSpec = tween(2000, easing = LinearOutSlowInEasing))
    //val exitTransition: ExitTransition = fadeOut(targetAlpha = 0f, animationSpec = tween(2000,  easing = LinearOutSlowInEasing))

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
            val loginViewmodel: LoginViewmodel = viewModel(navBackStackEntry) {
                LoginViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
                )
            }
            val intCompletedSignIn by loginViewmodel.intCompletedSignIn.collectAsStateWithLifecycle()
            val isSuccessfulWithAuthGoogle by loginViewmodel.isSuccessfulGoogleAuth.collectAsStateWithLifecycle()

            LoginScreen(
                { navController.navigate(RegistrationRoute) },
                { username, password ->
                    loginViewmodel.signIn(username, password)
                },
                intCompletedSignIn,
                /*isSuccessfulWithAuthGoogle,*/
                { loginViewmodel.setNumberCompletedSignInToZero() },
                reassignLoggedUser
                //{ launcher(context) { idToken -> loginViewmodel.authWithGoogle(idToken) } }
            )
        }

        composable<RegistrationRoute> { navBacStackEntry ->
            val registrationViewmodel: RegistrationViewmodel = viewModel(navBacStackEntry) {
                RegistrationViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase
                )
            }
            val userExist by registrationViewmodel.userExist.collectAsStateWithLifecycle()
            val emailExist by registrationViewmodel.emailExist.collectAsStateWithLifecycle()

            RegistrationScreen(
                { navController.popBackStack() },
                { name, username, password, email, birthdate, gender ->
                    navController.navigate(
                        RegistrationQuestionsRoute(name, username, password, email, birthdate, gender)
                    )
                },
                { username -> registrationViewmodel.userExist(username) },
                { email -> registrationViewmodel.emailExist(email) },
                userExist,
                emailExist,
                { registrationViewmodel.setUserExistToNull() },
                { registrationViewmodel.setEmailExistToNull() }
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
                user = User(username, password, email, name.ifEmpty { username }, birthdate, gender)
            }
            val thereIsErrorToAddUser by registrationQuestionsViewmodel.thereIsErrorToAddUser.distinctUntilChanged { old, new -> new == null }.collectAsStateWithLifecycle(null)
            val isSuccessMakeRoutines by registrationQuestionsViewmodel.isSuccessMakeRoutines.collectAsStateWithLifecycle()

            RegistrationQuestionsScreen(
                user,
                allQuestions,
                thereIsErrorToAddUser,
                isSuccessMakeRoutines,
                { userForAdd -> registrationQuestionsViewmodel.addUser(userForAdd) },
                { registrationQuestionsViewmodel.setThereIsErrorToNull() },
                { navController.navigate(LoginRoute) },
                { navController.popBackStack() },
                { answeredQuestions, emailUser -> registrationQuestionsViewmodel.saveUserTrainingRoutines(answeredQuestions, emailUser) }
            )
        }
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
            iconSelectedBottom = Icons.Default.Home,
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
fun GetLightGreen(): Color {
    return colorResource(R.color.lightGreen)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProyectoAppGymTheme {

    }
}