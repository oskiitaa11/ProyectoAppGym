package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.mapSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoappgym.GetLightGreen
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing

@SuppressLint("Range")
@Composable
fun LoginScreen(errorToSignIn: ErrorForShowing?, setErrorToSignIn: () -> Unit, onRegistrationScreen: () -> Unit, signIn: (String, String) -> Unit){
    var isClickedRegisterText by rememberSaveable { mutableStateOf(false) }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val allErrorsText = rememberSaveable(
        saver = mapSaver(
            save = { stateMap -> stateMap.toMap() },
            restore = { savedMap ->
                mutableStateMapOf<String, String>().apply {
                    putAll(savedMap as Map<String, String>)
                }
            })

    ) { mutableStateMapOf("email" to "", "password" to "") }
    val validateFieldsLogin: () -> Unit = {
        allErrorsText.forEach { (field, _) -> allErrorsText[field] = "" }
        validateEmailLogin(email) { errorText -> allErrorsText["email"] = errorText }
        validatePasswordLogin(password) { errorText -> allErrorsText["password"] = errorText }
    }
    var showWaitingDialog by rememberSaveable { mutableStateOf(false) }
    val stateScroll = rememberScrollState()

    var showIncorrectField by rememberSaveable { mutableStateOf(false) }

    ValidationIfShowingDialogsOnLogin(
        errorToSignIn,
        showWaitingDialog,
        { showWaitingDialog = it },
        { showIncorrectField = true },
        setErrorToSignIn
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.lightBlack))
            .verticalScroll(stateScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)) {
            Image(
                painter = painterResource(R.drawable.background_image1),
                contentDescription = "Background Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillBounds
            )
        }
        Column(verticalArrangement = Arrangement.Top) {
            Row {
                Text(
                    text = "Welcome to ",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "MyFitnessApp",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.lobster_two_bold_italic)),
                    color = GetLightGreen()
                )

            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowInputNormal(
                fieldValue = email,
                label = "Email",
                isError = showIncorrectField,
                idIcon = R.drawable.ic_email_24,
                error = allErrorsText["email"] ?: "",
                addNewFieldValue = { newText -> email = newText },
                validateUsernameField = { email -> validateEmailLogin(email, { errorText -> allErrorsText["email"] = errorText }) }
            )

            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(
                passwordValue = password,
                label = "Password",
                isError = showIncorrectField,
                error = allErrorsText["password"] ?: "",
                addNewPasswordValue = { newText -> password = newText },
                validatePasswordField = { validatePasswordLogin(password, { errorText -> allErrorsText["password"] = errorText }) }
            )

            Spacer(modifier = Modifier.height(30.dp))
            ShowButtonForLoginOrRegister("Sign in") {
                showWaitingDialog = true
                validateFieldsLogin()
                //Si los textos de error estan vacios, el usuario hará login
                if (allErrorsText.values.all { it.isEmpty() }) {
                    signIn(email.trim(), password)
                } else {
                    showWaitingDialog = false
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Text("¿Don't you have account? Create an account", color = GetLightGreen(), modifier = Modifier.clickable {
                onRegistrationScreen()
                isClickedRegisterText = true
            }, textDecoration = if (isClickedRegisterText) TextDecoration.Underline else TextDecoration.None)
            Spacer(modifier = Modifier.height(30.dp))
        }

    }

}

@Composable
fun ShowInputPassword(passwordValue: String, label: String, isError: Boolean = false, error: String, addNewPasswordValue: (String) -> Unit, validatePasswordField: ((String) -> Unit)?) {
    var canShowPassword by rememberSaveable { mutableStateOf(false) } //Variable para cambiar la contraseña
    var modifier = Modifier
        .height(70.dp)
        .padding(vertical = 8.dp)
        .border(
            2.dp,
            if (error.isNotEmpty() || isError) colorResource(R.color.red_error) else Color.LightGray,
            ShapeDefaults.ExtraSmall
        )
    var isFocusable by rememberSaveable { mutableStateOf(false) }

    modifier = if (validatePasswordField != null) modifier.onFocusChanged { focusState ->
        if (focusState.isFocused){
            isFocusable = true
        } else
            if (isFocusable) {
                validatePasswordField(passwordValue)
                isFocusable = false
            }
    } else modifier


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
            value = passwordValue,
            onValueChange = addNewPasswordValue,
            trailingIcon = {
                IconButton(
                    onClick = { canShowPassword = !canShowPassword }
                ) {
                    //Segun este la contraseña oculta o no se va cambiando el icono de visibilidad
                    Icon(painter = painterResource(if(canShowPassword) R.drawable.ic_visibility_off_24 else R.drawable.ic_visibility_24), contentDescription = "Icono para mostrar/ocultar la contraseña", tint = colorResource(R.color.lightBlack))
                }
            },
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_lock_24), contentDescription = "Icon password", tint = colorResource(R.color.lightBlack)) },
            shape = ShapeDefaults.ExtraSmall,
            //Con el visualTransformation le digo que se transforme el texto a asterisco o no
            visualTransformation = if(canShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White, focusedTextColor = Color.Black, unfocusedTextColor = Color.Black),
            modifier = modifier
        )

        ShowErrorText(error, 15.dp)
    }
}

@Composable
fun ShowButtonForLoginOrRegister(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = onClick,
        colors = ButtonColors(
            if(isPressed) colorResource(R.color.darkGreen) else colorResource(R.color.lightGreen),
            Color.White,
            Color.LightGray,
            Color.White
        ),
        shape = ShapeDefaults.Large,
        modifier = Modifier
            .width(300.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 5.dp),
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowErrorDialogForLogging(errorText: String, idIcon: Int, setErrorTextToNull: () -> Unit) {
    BasicAlertDialog(
        onDismissRequest = {  },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, ShapeDefaults.Medium),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(painterResource(idIcon), "info Icon", modifier = Modifier.size(32.dp))
                Text(errorText, color = colorResource(R.color.lightBlack), fontSize = 15.sp, modifier = Modifier.padding(horizontal = 10.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End
            ) {
                TextButton(onClick = setErrorTextToNull, colors = ButtonDefaults.textButtonColors().copy(containerColor = Color.Transparent)) {
                    Text("Close", color = colorResource(R.color.lightBlack), fontSize = 15.sp, textAlign = TextAlign.Left, modifier = Modifier.padding(horizontal = 10.dp))
                }
            }
        }
    }
}

@Composable
fun ValidationIfShowingDialogsOnLogin(errorToSignIn: ErrorForShowing?, showWaitingDialog: Boolean, setShowWaitingDialog: (Boolean) -> Unit, showIncorrectField: () -> Unit, setErrorToSignIn: () -> Unit) {

    if (errorToSignIn != null) {
        ShowErrorDialogForLogging(errorToSignIn.error, errorToSignIn.idIcon as Int, setErrorToSignIn)
        setShowWaitingDialog(false)
        if (errorToSignIn.idIcon == R.drawable.ic_invalid_credentials) showIncorrectField()
    }

    if(showWaitingDialog) ShowWaitingDialog("Accessing to Home")
}

fun validateEmailLogin(emailValue: String, changeErrorText: (String) -> Unit) {
    if(emailValue.isEmpty()) {
        changeErrorText("The field email can't be empty")
    } else if(!emailValue.trim().matches(Regex("(.*)\\b@gmail\\b(\\.)(es|com|net|org|edu|gov|info|int)"))) {
        changeErrorText("Insert a valid email")
    } else {
        changeErrorText("")
    }
}

fun validatePasswordLogin(passwordValue: String, changeErrorText: (String) -> Unit) {
    if(passwordValue.isEmpty()) {
        changeErrorText("The field password can't be empty")
    } else {
        changeErrorText("")
    }
}

