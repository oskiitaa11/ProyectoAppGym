package com.example.proyectoappgym.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.R
import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class ErrorRoute(val message: String, val idIcon: Int)

fun NavController.goToErrorDestination(error: String, idIcon: Int) {
    navigate(ErrorRoute(error, idIcon))
}

fun NavGraphBuilder.errorDestination() {
    composable<ErrorRoute> { navBackStackEntry ->
        val error: ErrorRoute = navBackStackEntry.toRoute()

        ErrorScreen(error.message, error.idIcon)
    }
}

@Composable
fun ErrorScreen(error: String, idIcon: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(horizontal = 20.dp)) {
        Image(painterResource(idIcon), "user_not found", modifier = Modifier.size(80.dp))
        Text(error, fontSize = 20.sp, lineHeight = 30.sp, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 15.dp))
    }

}