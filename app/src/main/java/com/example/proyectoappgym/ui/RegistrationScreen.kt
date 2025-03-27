package com.example.proyectoappgym.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.User
import kotlinx.serialization.Serializable

/*@Serializable
object RegistrationRoute

fun NavController.goToRegistrationScreen(){
    navigate(RegistrationRoute)
}

fun NavGraphBuilder.registrationDestination(onBack: () -> Unit) {
    composable<RegistrationRoute> {
        RegistrationScreen(onBack)
    }
}*/

@Composable
fun RegistrationScreen(onBack: () -> Unit, onRegistrationQuestion: (String, String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password1 by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    Scaffold(topBar = { TopAppBarRegistration(onBack) }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
            .fillMaxSize()
            .background(color = colorResource(R.color.lightBlack))
        ) {
            ShowInputNormal(name, "Name", R.drawable.ic_man_24, { newText -> name = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputNormal(username, "Username", R.drawable.ic_person_24, { newText -> username = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputEmail(email, { newValue -> email = newValue })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(password1, "You create a new password", { newText -> password1 = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(password2, "You repeat the new password", { newText -> password2 = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputBirthdate(birthdate, { newText -> birthdate = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowButtonForLoginOrRegister("Sign in", { onRegistrationQuestion(name, username, password1, email, birthdate) })

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarRegistration(onBack: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Back Icon")
            }
        },
        colors = TopAppBarColors(colorResource(R.color.lightBlack), colorResource(R.color.lightBlack), Color.White, Color.White, Color.White)
    )
}

@Composable
fun ShowInputNormal(fieldValue: String, label: String, idIcon: Int, addNewFieldValue: (String) -> Unit) {

    Column {
        Text(
            text = label,
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            value = fieldValue,
            onValueChange = addNewFieldValue,
            shape = ShapeDefaults.ExtraSmall,
            leadingIcon = { Icon(painter = painterResource(idIcon), contentDescription = "Icon for name") }
        )
    }
}

@Composable
fun ShowInputEmail(valueEmail: String, addNewValueEmail: (String) -> Unit) {
    Column {
        Text(
            text = "Email",
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            value = valueEmail,
            onValueChange = addNewValueEmail,
            shape = ShapeDefaults.ExtraSmall,
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_email_24), contentDescription = "Email icon") }
        )
    }
}

@Composable
fun ShowInputBirthdate(birthdateValue: String, addNewFieldValue: (String) -> Unit){
    var showCalendarDialog by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val datePickerDialog: DatePickerDialog

    Column {
        Text(
            text = "Birtdate",
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            value = birthdateValue,
            onValueChange = addNewFieldValue,
            readOnly = true,
            shape = ShapeDefaults.ExtraSmall,
            trailingIcon = {
                IconButton(onClick = { showCalendarDialog = true }) {
                    Icon(painter = painterResource(R.drawable.ic_calendar_month_24), contentDescription = "Calendar icon")
                }
            }
        )
    }

    if(showCalendarDialog) {
        datePickerDialog = DatePickerDialog(
            LocalContext.current,
            {
                    _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                addNewFieldValue("$dayOfMonth/${month+1}/$year")
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
        showCalendarDialog = false
    }


}

