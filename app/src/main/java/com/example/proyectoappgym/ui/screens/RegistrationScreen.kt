package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Gender
import java.time.Year

@Composable
fun RegistrationScreen(onBack: () -> Unit, onRegistrationQuestion: (String, String, String, String, String, Gender) -> Unit, askUserExist: (String) -> Unit, askEmailExist: (String) -> Unit, userExist: Boolean?, emailExist: Boolean?, setUserExistToNull: () -> Unit, setEmailExistToNull: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var birthdate by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("Oskiitaa15") }
    var password by remember { mutableStateOf("Swcdlcmokjoij89_") }
    var email by remember { mutableStateOf("oskiitaa12@gmail.com") }
    var gender by remember { mutableStateOf(Gender.NONE) }
    /*Se crea un mapa por cada campo se inserta el texto de error,
    inicialmente esta vacio. Cuando este vacio el campo es porque todavia
    no ha dado ningun error*/
    var allErrorsFields = remember {
        mutableStateMapOf("name" to "", "username" to "", "birthdate" to "", "password" to "", "email" to "", "gender" to "")
    }
    //Se valida cada campo para que el usuario cumpla con los requisitos minimos
    val validateFields: () -> Unit = {
        allErrorsFields.forEach { (field, _) -> allErrorsFields[field] = ""}
        validateTextUsername(
            username,
            username == email.split(Regex("@"))[0],
            { errorText -> allErrorsFields["username"] = errorText },
        )
        validatePassword(password, { errorText -> allErrorsFields["password"] = errorText })
        validateBirthdate(birthdate, { errorText -> allErrorsFields["birthdate"] = errorText })
        validateEmail(email, { errorText -> allErrorsFields["email"] = errorText })

    }

    // Si se ha preguntado si existe algun usuario o email, estas variables seran nulas y no entrará en el bloque
    if(userExist != null || emailExist != null){
        validateFieldsIfExist(
            emailExist ?: false, //Si emailExist es null significa que no se ha preguntado por este y significa que ya se ha preguntado antes
            userExist ?: false, //Si userlExist es null significa que no se ha preguntado por este y significa que ya se ha preguntado antes
            { errorText -> allErrorsFields["email"] = errorText },
            { errorText -> allErrorsFields["username"] = errorText },
            setUserExistToNull,
            setEmailExistToNull
            //Despues se asigna las dos variables a null otra vez
        )
        //Cuando se termine de validar los campos si no hay ningun error todos los valores del mapa estaran a null
        if(allErrorsFields.values.all { it.isEmpty() }) onRegistrationQuestion(name.trim(), username.trim(), password, email.trim(), birthdate, gender)
    }

    Scaffold(topBar = { TopAppBarRegistration(onBack) }) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
            .fillMaxSize()
            .background(color = colorResource(R.color.lightBlack))
        ) {
            ShowInputNormal(name, "Name(Optional)", R.drawable.ic_man_24, false, { newText -> name = newText })
            Spacer(modifier = Modifier.height(15.dp))
            ShowInputNormal(username, "Username", R.drawable.ic_person_24, allErrorsFields["username"]?.isNotEmpty() as Boolean, { newText -> username = newText })
            ShowErrorText(allErrorsFields["username"] as String, 5.dp)
            Spacer(modifier = Modifier.height(15.dp))
            ShowInputEmail(email, allErrorsFields["email"]?.isNotEmpty() as Boolean, { newValue -> email = newValue })
            ShowErrorText(allErrorsFields["email"] as String, 5.dp)
            Spacer(modifier = Modifier.height(15.dp))
            ShowInputPassword(passwordValue = password, label = "You create the new password", allErrorsFields["password"]?.isNotEmpty() as Boolean, addNewPasswordValue = { newText -> password = newText })
            ShowErrorText(allErrorsFields["password"] as String, 5.dp)
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 30.dp)
            ) {
                Column {
                    ShowInputBirthdate(birthdate, allErrorsFields["birthdate"]?.isNotEmpty() as Boolean, { newText -> birthdate = newText })
                }

                ShowGender(gender, { newGender ->  gender = newGender})
            }
            ShowErrorText(allErrorsFields["birthdate"] as String, 0.dp, 15.dp)
            Spacer(modifier = Modifier.height(15.dp))
            /*Se comprobará si todos los valores del mapa estan vacios, si es asin, no ha habido ningun error
            y se pasara a la pantalla de preguntas*/
            ShowButtonForLoginOrRegister(
                "Register",
                {
                    validateFields()
                    if(allErrorsFields.values.all { it.isEmpty() }) {
                        askUserExist(username)
                        askEmailExist(email)
                    }
                }
            )

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

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ShowErrorText(errorText: String, startPadding: Dp, endPadding: Dp = 0.dp) {

    if(errorText.isNotEmpty()) {//Si el texto de error esta vacio es porque no ha habido ningun error
        Row(
            modifier = Modifier.fillMaxWidth(0.8f).padding(start = startPadding, top = 2.dp),
            verticalAlignment = Alignment.Top    ,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_error_outline_16),
                contentDescription = "Error icon",
                modifier = Modifier.padding(end = 5.dp),
                tint = colorResource(R.color.red_error)
            )
            Text(
                errorText,
                color = colorResource(R.color.red_error),
                fontStyle = FontStyle.Italic,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                lineHeight = 12.sp,
                modifier = Modifier.padding(end = endPadding)
            )
        }
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
            "Sex(Optional)",
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
fun ShowInputNormal(fieldValue: String, label: String, idIcon: Int, isError: Boolean, addNewFieldValue: (String) -> Unit) {

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
                .border(2.dp, if(isError) colorResource(R.color.red_error) else Color.Black, ShapeDefaults.ExtraSmall)
        )
    }
}

@Composable
fun ShowInputEmail(valueEmail: String, isError: Boolean, addNewValueEmail: (String) -> Unit) {
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
                .border(2.dp, if(isError) colorResource(R.color.red_error) else Color.Black, ShapeDefaults.ExtraSmall),
        )
    }
}

@Composable
fun ShowInputBirthdate(birthdateValue: String, isError: Boolean, addNewFieldValue: (String) -> Unit){
    var showCalendarDialog by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val datePickerDialog: DatePickerDialog

    Column {
        Text(
            text = "Birthdate",
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
            modifier = Modifier.width(150.dp)
                .height(50.dp)
                .border(2.dp, if(isError) colorResource(R.color.red_error) else Color.Black, ShapeDefaults.ExtraSmall)
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

fun validateTextUsername(valueText: String, usernameIsEqualToEmail: Boolean, changeValueField: (String) -> Unit) {
    val insertedEspecialCharacters: String

    if(valueText.isEmpty()) {
        changeValueField("The username can't be empty")
        return
    } else if(valueText.length <= 5) {
        changeValueField("Must be greater than 5 characters")
        return
    } else if(valueText.length > 20) {
        changeValueField("Must be less than 20 characters")
        return
    } else if(!valueText.contains(Regex("[A-Z]"))) {
        changeValueField("Must have at least one upper case")
        return
    } else if(valueText.contains(Regex("['´`\"\\-+\\\\/@<>&;^#=():%*|]"))) {
        insertedEspecialCharacters = valueText.filter { it.toString().matches(Regex("[|'´`\"+\\\\/<>;&^#=():%*]")) }
        changeValueField("Invalid characters: $insertedEspecialCharacters")
        return
    } else if(usernameIsEqualToEmail) {
        changeValueField("The username can't be equal than the email")
        return
    }
}

fun validatePassword(valuePassword: String, changeValueField: (String) -> Unit) {
    if(valuePassword.isEmpty()) {
        changeValueField("The field password can't be empty")
        return
    } else if(valuePassword.length < 10) {
        changeValueField("Must have between more tha 10 characters")
        return
    } else if(valuePassword.length > 50) {
        changeValueField("Must have between more tha 50 characters")
        return
    } else if(!(valuePassword.contains(Regex("[A-Z]")))) {
        changeValueField("Must have at least one upper case")
        return
    } else if(!(valuePassword.contains(Regex("[a-z]")))) {
        changeValueField("Must have at least one lower case")
        return
    } else if(!(valuePassword.contains(Regex("[0-9]")))) {
        changeValueField("Must have at least one number")
        return
    } else if (!(valuePassword.contains(Regex("[!\"\$#%&'\\\\()*+,\\-./;:<=>?@^\\[\\]_{|}~`]")))) {
        changeValueField("Must have special characters")
    }

}

fun validateBirthdate(valueBirthdate: String, changeValueField: (String) -> Unit) {
    if(valueBirthdate.isEmpty()) {
        changeValueField("The field birthdate can't be empty")
        return
    } else if(valueBirthdate.split("/").last().toInt() == Year.now().value) {
        changeValueField("Must be previous to this year")
    }
}

fun validateEmail(valueEmail: String, changeValueField: (String) -> Unit) {
    if(valueEmail.isEmpty()) {
        changeValueField("The field email can't be empty")
        return
    } else if(!valueEmail.trim().matches(Regex("(.*)\\b@gmail\\b(\\.)(es|com|net|org|edu|gov|info|int)"))) {
        changeValueField("Insert a valid email")
    }
}


fun validateFieldsIfExist(emailExist: Boolean, userExist: Boolean, changeErrorTextEmail: (String) -> Unit, changeErrorTextUser: (String) -> Unit, setUserExistToNull: () -> Unit, setEmailExistToNull: () -> Unit) {
    valueExist(emailExist, changeErrorTextEmail, setEmailExistToNull, "Email already registered")
    valueExist(userExist, changeErrorTextUser, setUserExistToNull, "Username already registered")
}

fun valueExist(fieldExist: Boolean, changeErrorTextField: (String) -> Unit, setFieldExistToNull: () -> Unit, errorText: String){
    if(fieldExist) changeErrorTextField(errorText)
    setFieldExistToNull()
}