package com.example.proyectoappgym.ui.screens

import android.content.Context
import android.widget.Toast
import android.widget.Toast.makeText
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun LoginScreen(onRegistrationScreen: () -> Unit, signIn: (String, String) -> Unit){
    var isClickedRegisterText by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val allErrorsText = remember { mutableStateMapOf("email" to "", "password" to "") }
    val validateFieldsLogin: () -> Unit = {
        allErrorsText.forEach { (field, _) -> allErrorsText[field] = "" }
        validateEmailLogin(email) { errorText -> allErrorsText["email"] = errorText }
        validatePasswordLogin(password) { errorText -> allErrorsText["password"] = errorText }
    }
    var showWaitingDialog by remember { mutableStateOf(false) }
    var stateScroll = rememberScrollState()

    /*if(intCompletedSignIn>0) ValidateSignIn(
        intCompletedSignIn,
        { errorText -> allErrorsText["email"] = errorText },
        { errorText ->
            allErrorsText["email"] = errorText
            allErrorsText["password"] = errorText
        },
        setNumberToZero,
        reassignLoggedUser,
        { showWaitingDialog = false }
    )*/

    if(showWaitingDialog) ShowWaitingDialog("Accessing to Home")

    Column(
        modifier = Modifier.fillMaxSize().background(color = colorResource(R.color.lightBlack)).verticalScroll(stateScroll),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
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
            modifier = Modifier.fillMaxSize().padding(top = 30.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowInputEmail(email, allErrorsText["email"]?.isNotEmpty() as Boolean, { newText -> email = newText })
            ShowErrorText(allErrorsText["email"] as String, 5.dp)
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(passwordValue = password, label = "Password", isError = allErrorsText["password"]?.isNotEmpty() as Boolean, addNewPasswordValue = { newText -> password = newText })
            ShowErrorText(allErrorsText["password"] as String, 5.dp)
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
            Text(" - - - - - -  OR SIGN IN WITH  - - - - - - ", color = Color.White, fontSize = 20.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(30.dp))
            ShowButtonForGoogleLogout {
                //launcher()
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

    }

}

@Composable
fun ShowInputPassword(passwordValue: String, label: String, isError: Boolean, addNewPasswordValue: (String) -> Unit) {
    var canShowPassword by remember { mutableStateOf(false) } //Variable para cambiar la contraseña

    Column {
        Text(
            text = label,
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
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
            modifier = Modifier.height(70.dp).padding(vertical = 8.dp)
                .border(2.dp, if(isError) colorResource(R.color.red_error) else Color.LightGray, ShapeDefaults.ExtraSmall)
        )
    }
}

@Composable
fun ShowButtonForLoginOrRegister(text: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    TextButton(
        onClick = onClick,
        colors = ButtonColors(if(isPressed) colorResource(R.color.lightGreen).copy(alpha = 0.8f) else colorResource(R.color.lightGreen), Color.White, Color.LightGray, Color.LightGray),
        shape = ShapeDefaults.Large,
        interactionSource = interactionSource,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 5.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowButtonForGoogleLogout(onClick: () -> Unit) {
    Box(modifier = Modifier.background(Color.White, shape = ShapeDefaults.Small)) {

    Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = "Google Icon",
            modifier = Modifier.padding(10.dp)
        )

    }
}

fun validateEmailLogin(emailValue: String, changeErrorText: (String) -> Unit) {
    if(emailValue.isEmpty()) {
        changeErrorText("The field email can't be empty")
    } else if(!emailValue.trim().matches(Regex("(.*)\\b@gmail\\b(\\.)(es|com|net|org|edu|gov|info|int)"))) {
        changeErrorText("Insert a valid email")
    }
}

fun validatePasswordLogin(passwordValue: String, changeErrorText: (String) -> Unit) {
    if(passwordValue.isEmpty()) {
        changeErrorText("The field password can't be empty")
    }
}

/*@Composable
fun ValidateSignIn(changeErrorTextEmail: (String) -> Unit, changeErrorTextInFields: (String) -> Unit, hideWaitingDialog: () -> Unit) {
    val context = LocalContext.current

    LaunchedEffect(intCompletedSignIn) {
       when(intCompletedSignIn) {
           1 -> {
               showToast("Started Session", context)
               reassignLoggedUser()//Cuando se termine la parte del perfil, terminar de desarrollar este metodo
           }
           2 -> changeErrorTextEmail("Email not registered")
           3 -> changeErrorTextInFields("User not found")
           4 -> showToast("Error to the sign in", context)
       }
        hideWaitingDialog()
        setNumberToZero()
    }
}*/


fun showToast(text: String, context: Context) {
    makeText(context, text, Toast.LENGTH_SHORT).show()
}
/*fun getCredential(context: Context) {
    val googleIdOption = GetGoogleIdOption.Builder()
        // Your server's client ID, not your Android client ID.
        .setServerClientId(getString(context, R.string.default_web_client_id))
        // Only show accounts previously used to sign in.
        .setFilterByAuthorizedAccounts(true)
        .build()

// Create the Credential Manager request
    val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()
}*/

