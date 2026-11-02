package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.LoginRoute
import com.example.proyectoappgym.R
import com.example.proyectoappgym.RegistrationQuestionsRoute
import com.example.proyectoappgym.RegistrationRoute
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.users.Gender
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.graphs.RegistrationGraphRoute
import com.example.proyectoappgym.ui.viewmodels.RegistrationViewmodel
import com.example.proyectoappgym.ui.viewmodels.SharedRegistrationViewmodel
import kotlinx.coroutines.flow.distinctUntilChanged
import java.time.Year
import java.util.Locale

fun NavGraphBuilder.registrationDestination(navController: NavController) {
    composable<RegistrationRoute> { navBackStackEntry ->
        val parentEntry = remember(navBackStackEntry) {
            navController.getBackStackEntry(RegistrationGraphRoute)
        }
        val sharedRegistrationViewmodel : SharedRegistrationViewmodel = hiltViewModel(parentEntry)
        val registrationViewmodel: RegistrationViewmodel = hiltViewModel()
        val emailExists by sharedRegistrationViewmodel.emailExist.collectAsStateWithLifecycle()
        val userWithExistedEmail by sharedRegistrationViewmodel.userWithExistedEmail.collectAsStateWithLifecycle()
        val errorWithUsername by registrationViewmodel.errorWithUsername.collectAsStateWithLifecycle()
        val serverNotConnected by registrationViewmodel.serverNotConnected.collectAsStateWithLifecycle()
        val errorToAddUser: ErrorForShowing? by sharedRegistrationViewmodel.errorToAddUser.collectAsStateWithLifecycle(null)
        val context = LocalContext.current
        val fieldUsername by registrationViewmodel.fieldUsername.collectAsStateWithLifecycle()
        val isFocusable by registrationViewmodel.isFocusable.collectAsStateWithLifecycle()

        LaunchedEffect(sharedRegistrationViewmodel.userCreatedDone) {
            sharedRegistrationViewmodel.userCreatedDone.distinctUntilChanged().collect {
                if (it) {
                    navController.popBackStack(LoginRoute, false)
                    Toast.makeText(context, "User created", Toast.LENGTH_LONG)
                }
            }
        }

        RegistrationScreen(
            emailExists,
            errorWithUsername,
            errorToAddUser,
            userWithExistedEmail,
            serverNotConnected,
            isFocusable,
            fieldUsername,
            { isFocusable -> registrationViewmodel.updateIsFocusable(isFocusable) },
            { sharedRegistrationViewmodel.setErrorToAddUserToNull() },
            {
                sharedRegistrationViewmodel.setExistsEmailToNull()
            },
            { registrationViewmodel.setServerNotConnectedToFalse() },
            { navController.popBackStack() },
            { name, username, password, email, birthdate, gender ->
                navController.navigate(RegistrationQuestionsRoute(name, username, password, email, birthdate, gender))
            },
            { user -> sharedRegistrationViewmodel.addUser(user) },
            { registrationViewmodel.updateFieldUsername(it) },
            { newEmail -> registrationViewmodel.updateEmail(newEmail) },
            { registrationViewmodel.callToVerifyUsername() }
        )
    }
}

@Composable
fun RegistrationScreen(
    emailExist: Boolean?,
    errorFieldUsername: String,
    errorToAddUser: ErrorForShowing?,
    userToCreate: User?,
    serverNotConnected: Boolean,
    hasFocus: Boolean,
    fieldUsername: String,
    updateIsFocusable: (Boolean) -> Unit,
    setErrorToAddUser: () -> Unit,
    setExistsEmailAndUsernameToFalse: () -> Unit,
    setServerNotConnected: () -> Unit,
    onBack: () -> Unit,
    onRegistrationQuestion: (String, String, String, String, String, Gender) -> Unit, addUser: (User) -> Unit,
    updateFieldUsername: (String) -> Unit,
    updateEmail: (String) -> Unit,
    verifyUsername: () -> Unit
) {
    val stateScroll = rememberScrollState()
    var name by rememberSaveable { mutableStateOf(userToCreate?.name ?: "") }
    var birthdate by rememberSaveable { mutableStateOf(userToCreate?.birthdate ?: "") }
    var password by rememberSaveable { mutableStateOf(userToCreate?.password ?: "") }
    var email by rememberSaveable { mutableStateOf(userToCreate?.email ?: "") }
    var gender by rememberSaveable { mutableStateOf(userToCreate?.gender ?: Gender.NONE) }
    /*Se crea un mapa por cada campo se inserta el texto de error,
    inicialmente esta vacio. Cuando este vacio el campo es porque todavia
    no ha dado ningun error*/
    val allErrorsFields = rememberSaveable(
            saver = mapSaver(
                save = { stateMap -> stateMap.toMap() },
                restore = { savedMap ->
                    mutableStateMapOf<String, String>().apply {
                        putAll(savedMap as Map<String, String>)
                    }
                })
            ) {
        mutableStateMapOf("name" to "", "birthdate" to "", "password" to "", "email" to "", "gender" to "")
    }
    var newUserToCreate: User
    var validationsDone by rememberSaveable { mutableStateOf(false) }
    //Se valida cada campo para que el usuario cumpla con los requisitos minimos
    val goToNextScreen: () -> Unit = {
        if(allErrorsFields.values.all { it.isEmpty() })
            if (userToCreate != null) {
                newUserToCreate = User(
                    userToCreate.username,
                    userToCreate.password,
                    email.lowercase().trim(),
                    userToCreate.name,
                    userToCreate.birthdate,
                    userToCreate.gender,
                    userToCreate.allQuestionsAnswered,
                    userToCreate.idProfileAvatar,
                    userToCreate.trainingRoutines
                )
                addUser(newUserToCreate)
            } else
                onRegistrationQuestion(name.trim().ifEmpty { fieldUsername.trim() }, fieldUsername.trim(), password, email.lowercase().trim(), birthdate, gender)
    }
    var showWaitingDialog by rememberSaveable { mutableStateOf(false) }
    var fieldUsernameIsEmpty by rememberSaveable { mutableStateOf(false) }
    val validateAllFields: () -> Unit = {
        validateEmail(
            email.trim(),
            fieldUsername.trim() == email.split(Regex("@"))[0],
            { errorText -> allErrorsFields["email"] = errorText })

        validatePassword(password, { errorText -> allErrorsFields["password"] = errorText })
        validateBirthdate(birthdate, { errorText -> allErrorsFields["birthdate"] = errorText })
    }
    val focus = LocalFocusManager.current

    // Si se ha preguntado si existe algun usuario o email, estas variables seran nulas y no entrará en el bloque
       validateFieldsIfExist(
           emailExist, //Si emailExist es null significa que no se ha preguntado por este y significa que ya se ha preguntado antes
           //Si userlExist es null significa que no se ha preguntado por este y significa que ya se ha preguntado antes
           { errorText -> allErrorsFields["email"] = errorText },
           //Despues se asigna las dos variables a null otra vez
           { showWaitingDialog = false },
           setExistsEmailAndUsernameToFalse
       )

    ValidatingIfShowingDialog(
        showWaitingDialog,
        userToCreate,
        serverNotConnected,
        errorToAddUser,
        setServerNotConnected,
        setErrorToAddUser,
        { showWaitingDialog = it },
        onBack
    )

    if (validationsDone) {
        if (errorFieldUsername.isEmpty()) {
            goToNextScreen()
            showWaitingDialog = false
            validationsDone = false
        }
    }

    Scaffold(
        topBar = { ShowSimpleTopAppBar("Register user", onBack) },
        floatingActionButton = {
            ShowButtonForLoginOrRegister(
                "Register",
                {
                    showWaitingDialog = true
                    validateAllFields()
                    if(!allErrorsFields.values.all { it.isEmpty() } || errorFieldUsername.isNotEmpty()) {
                        showWaitingDialog = false
                    } else goToNextScreen()
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = colorResource(R.color.lightBlack)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = colorResource(R.color.lightBlack))
                .verticalScroll(stateScroll)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focus.clearFocus()
                    }
                }

        ) {
            ShowInputNormal(
                fieldValue = name,
                label = "Name(Optional)",
                idIcon = R.drawable.ic_man_24,
                error = "",
                addNewFieldValue = { newText -> name = newText },
                validateUsernameField = null
            )
            Spacer(modifier = Modifier.height(15.dp))
            ShowInputUsername(
                fieldValue = fieldUsername,
                label = "Username",
                idIcon = R.drawable.ic_person_24,
                addNewFieldValue = { newText -> updateFieldUsername(newText) },
                hasFocus,
                if (errorFieldUsername.isNotEmpty()) errorFieldUsername else if (fieldUsernameIsEmpty) "The username can not be empty" else "",
                updateIsFocusable,
                {
                    fieldUsernameIsEmpty = fieldUsername.isEmpty()
                    if (!fieldUsernameIsEmpty) verifyUsername()
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            ShowInputNormal(
                fieldValue = email,
                label = "Email",
                idIcon = R.drawable.ic_email_24,
                error = allErrorsFields["email"] ?: "",
                addNewFieldValue = { newValue -> email = newValue },
                validateUsernameField = { email ->
                    validateEmail(
                        email.trim(),
                        fieldUsername.trim() == email.split(Regex("@"))[0],
                        { errorText -> allErrorsFields["email"] = errorText }
                    )
                    if (allErrorsFields["email"]?.isEmpty() == true) updateEmail(email)
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            ShowInputPassword(
                passwordValue = password,
                label = "You create the new password",
                error = allErrorsFields["password"] ?: "",
                addNewPasswordValue = { newText -> password = newText },
                validatePasswordField = { password -> validatePassword(password, { errorText -> allErrorsFields["password"] = errorText })}
            )

            Spacer(modifier = Modifier.height(15.dp))
            ShowInputBirthdate(birthdate,
                allErrorsFields["birthdate"] ?: "",
                { newText -> birthdate = newText },
                { birthdate -> validateBirthdate(birthdate,
                    { errorBirthdate -> allErrorsFields["birthdate"] = errorBirthdate }) })

            Spacer(Modifier.height(15.dp))
            ShowGender(gender) { newGender -> gender = newGender }
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ShowErrorText(errorText: String, startPadding: Dp, endPadding: Dp = 0.dp) {

    if(errorText.isNotEmpty()) {//Si el texto de error esta vacio es porque no ha habido ningun error
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(start = startPadding, top = 2.dp),
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
        //Si le asigno 1fr me deja mucho espacio a los lados y el contenido del padre no se ve visualmente centrado
        modifier = Modifier.padding(bottom = 15.dp)
    ) {
        Text(
            "Sex(Optional)",
            fontStyle = FontStyle.Italic,
            modifier = Modifier.padding(bottom = 5.dp),
            color = Color.White,
        )

        Row(horizontalArrangement = Arrangement.Start) {
            ShowGenderButton(colorResource(R.color.blue), Gender.M, gender == Gender.M, R.drawable.ic_man_24, changeGender)
            ShowGenderButton(colorResource(R.color.pink), Gender.F, gender == Gender.F, R.drawable.ic_woman_24, changeGender)
            ShowGenderButton(Color.White, Gender.IND, gender == Gender.IND, R.drawable.ic_transgender_24, changeGender)
        }
    }
}

@Composable
fun ShowInputNormal(fieldValue: String, label: String, idIcon: Int, isError: Boolean = false, error: String, addNewFieldValue: (String) -> Unit, validateUsernameField: ((String) -> Unit)?) {
    var modifierForText = Modifier
        .height(70.dp)
        .padding(vertical = 8.dp)
        .border(
            2.dp,
            if (error.isNotEmpty() || isError) colorResource(R.color.red_error) else Color.LightGray,
            ShapeDefaults.ExtraSmall
        )
    var isFocusable by rememberSaveable { mutableStateOf(false) }

    modifierForText = if (validateUsernameField != null) modifierForText.onFocusChanged { focusState ->
        if (focusState.hasFocus) {
            isFocusable = true
        } else if(isFocusable) {
            validateUsernameField(fieldValue)
            isFocusable = false
        }
    } else modifierForText

    Column(
        modifier = Modifier.width(300.dp).widthIn(max = 400.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 15.dp, bottom = 10.dp).fillMaxWidth()
        )

        TextField(
            value = fieldValue,
            onValueChange = addNewFieldValue,
            shape = ShapeDefaults.ExtraSmall,
            leadingIcon = { Icon(painter = painterResource(idIcon), contentDescription = "Icon for name", tint = colorResource(R.color.lightBlack)) },
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
            modifier = modifierForText
        )

        ShowErrorText(error, 15.dp)
    }
}

@Composable
fun ShowInputBirthdate(birthdateValue: String, error: String, addNewFieldValue: (String) -> Unit, validateBirthdateField: (String) -> Unit){
    var showCalendarDialog by rememberSaveable { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val datePickerDialog: DatePickerDialog
    val modifier = Modifier
        .height(70.dp)
        .padding(vertical = 8.dp)
        .border(
            2.dp,
            if (error.isNotEmpty()) colorResource(R.color.red_error) else Color.LightGray,
            ShapeDefaults.ExtraSmall
        )

    Column(
        modifier = Modifier.width(300.dp).widthIn(max = 400.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Birthdate",
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 15.dp, bottom = 10.dp).fillMaxWidth()
        )

        TextField(
            value = birthdateValue,
            onValueChange =  { newBirthdate ->  },
            readOnly = true,
            shape = ShapeDefaults.ExtraSmall,
            trailingIcon = {
                IconButton(onClick = { showCalendarDialog = true }) {
                    Icon(painter = painterResource(R.drawable.ic_calendar_month_24), contentDescription = "Calendar icon", tint = colorResource(R.color.lightBlack))
                }
            },

            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
            modifier = modifier
        )

        ShowErrorText(error, 15.dp)
    }

    if(showCalendarDialog) {
        datePickerDialog = DatePickerDialog(
            LocalContext.current,
            {
                    _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                // Formatear la fecha como dd/MM/yyyy
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = formatter.format(selectedCalendar.time)
                addNewFieldValue(formattedDate)
                validateBirthdateField(formattedDate)
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
fun ShowInputUsername(fieldValue: String, label: String, idIcon: Int, addNewFieldValue: (String) -> Unit, hasFocus: Boolean, error: String, updateIsFocusable: (Boolean) -> Unit, validateUsernameField: () -> Unit) {
    var modifierForText = Modifier
        .height(70.dp)
        .padding(vertical = 8.dp)
        .border(
            2.dp,
            if (error.isNotEmpty()) colorResource(R.color.red_error) else Color.LightGray,
            ShapeDefaults.ExtraSmall
        )

    modifierForText = modifierForText.onFocusChanged { focus ->
        if (focus.hasFocus)
            updateIsFocusable(true)
        else if (hasFocus) {
            updateIsFocusable(false)
            validateUsernameField()
        }

    }

    Column(
        modifier = Modifier.width(300.dp).widthIn(max = 400.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(start = 15.dp, bottom = 10.dp).fillMaxWidth()
        )

        TextField(
            value = fieldValue,
            onValueChange = addNewFieldValue,
            shape = ShapeDefaults.ExtraSmall,
            leadingIcon = { Icon(painter = painterResource(idIcon), contentDescription = "Icon for name", tint = colorResource(R.color.lightBlack)) },
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
            modifier = modifierForText
        )

        ShowErrorText(error, 15.dp)
    }
}

@Composable
fun ShowGenderButton(color: Color, gender: Gender, isSelectedActualGender: Boolean, idIcon: Int, changeActualGender: (Gender) -> Unit) {
    val getColorToggled: (genderButton: Gender, colorButton: Color) -> Color = { genderButton, colorButton ->
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

@Composable
fun ValidatingIfShowingDialog(showWaitingDialog: Boolean, userToCreate: User?, serverNotConnected: Boolean, errorToAddUser: ErrorForShowing?, setServerNotConnected: () -> Unit, setErrorToAddUser: () -> Unit, setShowWaitingDialog: (Boolean) -> Unit, onBack: () -> Unit) {
    if(showWaitingDialog) ShowWaitingDialog(if (userToCreate != null) "Adding user" else "Heading to questions")

    if (serverNotConnected) ShowErrorDialogForLogging(
        "Unable to connect to the server",
        R.drawable.ic_not_connecting,
        if (userToCreate != null) setServerNotConnected else onBack
    )

    if (errorToAddUser != null) {
        ShowErrorDialogForLogging(errorToAddUser.error, errorToAddUser.idIcon as Int, setErrorToAddUser)
        setShowWaitingDialog(false)
    }
}

fun validateTextUsername(valueText: String, usernameIsEqualToEmail: Boolean, changeValueField: (String) -> Unit): Boolean {
    val insertedEspecialCharacters: String

     if(valueText.length <= 5) {
        changeValueField("Must be greater than 5 characters")
        return true
    } else if(valueText.length > 20) {
        changeValueField("Must be less than 20 characters")
        return true
    } else if(valueText.contains(Regex("['´`\"\\-+\\\\/@<>&;^#=():%*|]"))) {
        insertedEspecialCharacters = valueText.filter { it.toString().matches(Regex("[|'´`\"+\\\\/<>;&^#=():%*]")) }
        changeValueField("Invalid characters: $insertedEspecialCharacters")
        return true
    } else if(usernameIsEqualToEmail) {
        changeValueField("The username can't be equal than the email")
        return true
    } else {
         return false
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
    } else {
        changeValueField("")
    }

}

fun validateBirthdate(valueBirthdate: String, changeValueField: (String) -> Unit) {
    if(valueBirthdate.isEmpty()) {
        changeValueField("The field birthdate can't be empty")
        return
    } else if(valueBirthdate.split("/").last().toInt() == Year.now().value) {
        changeValueField("Must be previous to this year")
    } else {
        changeValueField("")
    }
}

fun validateEmail(valueEmail: String, emailIsEqualToUsername: Boolean, changeValueField: (String) -> Unit) {
    if(valueEmail.isEmpty()) {
        changeValueField("The field email can't be empty")
        return
    } else if(!valueEmail.trim().matches(Regex("(.*)\\b@gmail\\b(\\.)(es|com|net|org|edu|gov|info|int)"))) {
        changeValueField("Insert a valid email")
    } else if(emailIsEqualToUsername) {
        changeValueField("Username can't be equal than email")
    } else {
        changeValueField("")
    }
}


fun validateFieldsIfExist(emailExist: Boolean?, changeErrorTextEmail: (String) -> Unit, hideWaitingDialog: () -> Unit, setExistsFieldToFalse: () -> Unit) {
    if (emailExist != null)
        valueExist(emailExist, changeErrorTextEmail, "Email already registered", hideWaitingDialog)

    setExistsFieldToFalse()
}

fun valueExist(fieldExist: Boolean, changeErrorTextField: (String) -> Unit, errorText: String, hideWaitingDialog: () -> Unit){
    if(fieldExist) {
        changeErrorTextField(errorText)
        hideWaitingDialog()
    } else {
        changeErrorTextField("")
    }
}