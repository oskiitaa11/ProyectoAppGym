package com.example.proyectoappgym.ui

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.serialization.generateRouteWithArgs
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Gender

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
fun RegistrationScreen(onBack: () -> Unit, onRegistrationQuestion: (String, String, String, String, String, Gender) -> Unit) {
    var name by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.NONE) }
    val getInitialErrorsFieldsMap: (List<String>) -> List<Pair<String, String>> = {
        it.map { field -> Pair(field, "") }
    }
    var allErrorsFields = remember {
        SnapshotStateMap<String, String>().apply {
            putAll(getInitialErrorsFieldsMap(listOf("name", "username", "birthdate", "password", "email", "gender")))
        }
    }

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
            ShowErrorText(allErrorsFields["username"] as String)
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputEmail(email, { newValue -> email = newValue })
            
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(passwordValue = password, label = "You create the new password", heightField = 50.dp, addNewPasswordValue = { newText -> password = newText })
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                ShowInputBirthdate(birthdate, { newText -> birthdate = newText })
                ShowGender(gender, { newGender ->  gender = newGender})
            }
            Spacer(modifier = Modifier.height(20.dp))
            ShowButtonForLoginOrRegister("Sign in", { onRegistrationQuestion(name, username, password, email, birthdate, gender) })

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
fun ShowErrorText(errorText: String) {
    if(errorText.isNotEmpty()) {//Si el texto de error esta vacio es porque no ha habido ningun error
        Row(
            modifier = Modifier.fillMaxWidth(0.9f),
            verticalAlignment = Alignment.Top    ,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error_outline_16),
                contentDescription = "Error icon",
                modifier = Modifier.padding(top = 2.dp, end = 5.dp),
                tint = colorResource(R.color.red_error)
            )
            Text(errorText, color = colorResource(R.color.red_error), fontStyle = FontStyle.Italic, fontSize = 12.sp)
        }
    }
}

fun getErrorTextUsername(valueText: String, usernameIsEqualToEmail: Boolean, changeValueField: (String) -> Unit){
    if(valueText.isEmpty()) {
        changeValueField("The username can't be empty")
    } else if(valueText.length <= 5) {
        changeValueField("The username must be greater than 5 characters")
    } else if(valueText.length <= 20) {
        changeValueField("The username must be less than 20 characters")
    } else if(!valueText.contains(Regex("[A-Z]"))) {
        changeValueField("The username must have at least one upper case")
    } else if(valueText.contains(Regex("['´`\"-+\\\\/@<>&;^#=():%*|]"))) {
        changeValueField("The username can't have '´`\"-+\\\\/@<>&;^#=():%*|")
    } else if(usernameIsEqualToEmail) {
        changeValueField("The username can't be equal than the email")
    }
}

@Composable
fun ShowGender(gender: Gender, changeGender: (Gender) -> Unit) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        //Si le asigno 1fr me deja mucho espacio a los lados y el contenido del padre no se ve visualmente centrado
        modifier = Modifier.fillMaxWidth(1f).padding(start = 20.dp)
    ) {
        Text(
            "Sex",
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 5.dp),
            color = Color.White,
        )

        Row(horizontalArrangement = Arrangement.Center) {
            ShowGenderButton(colorResource(R.color.blue), Gender.M, gender == Gender.M, R.drawable.ic_man_24, changeGender)
            ShowGenderButton(colorResource(R.color.pink), Gender.F, gender == Gender.F, R.drawable.ic_woman_24, changeGender)
            ShowGenderButton(Color.White, Gender.IND, gender == Gender.IND, R.drawable.ic_transgender_24, changeGender)
        }
    }
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
            leadingIcon = { Icon(painter = painterResource(idIcon), contentDescription = "Icon for name") },
            modifier = Modifier.height(50.dp)
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
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_email_24), contentDescription = "Email icon") },
            modifier = Modifier.height(50.dp)
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
            },
            modifier = Modifier.width(150.dp).height(50.dp)
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

@Composable
fun ShowGenderButton(color: Color, gender: Gender, isSelectedActualGender: Boolean, idIcon: Int, changeActualGender: (Gender) -> Unit) {
    var getColorToggled: (genderButton: Gender, colorButton: Color) -> Color = { genderButton, colorButton ->
        if(isSelectedActualGender) colorButton.copy(alpha = 0.3f) else colorButton
    }

    Card(
        modifier = Modifier.toggleable(
            value = isSelectedActualGender,
            onValueChange = { changeActualGender(gender) }
        ),
        colors = CardColors(getColorToggled(gender, color),  if(gender==Gender.IND) Color.Black else Color.White, Color.LightGray, Color.LightGray),
        shape = ShapeDefaults.ExtraSmall
    ) {
        Icon(
            painter = painterResource(idIcon),
            contentDescription = "Masculine icon",
            modifier = Modifier.padding(10.dp, 5.dp)
        )
    }
}

