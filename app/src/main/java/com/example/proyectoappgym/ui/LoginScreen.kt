package com.example.proyectoappgym.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Person
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoappgym.R
import java.lang.Error

/*@Serializable
object LoginRoute

fun NavGraphBuilder.loginDestination(onRegistrationScreen: () -> Unit, onHomeScreen: () -> Unit) {
    composable<LoginRoute> {
        LoginScreen(onRegistrationScreen, onHomeScreen)
    }
}*/

@Composable
fun LoginScreen(onRegistrationScreen: () -> Unit){
    var isClickedRegisterText by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().background(color = colorResource(R.color.lightBlack)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            ShowInputUsername(username, { newText -> username = newText })
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputPassword(passwordValue = password, label = "Password", isError = false, addNewPasswordValue = { newText -> password = newText })
            Spacer(modifier = Modifier.height(30.dp))
            ShowButtonForLoginOrRegister(
                "Login",
                {  }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text("¿Don't you have account? You registrate", color = GetLightGreen(), modifier = Modifier.clickable {
                onRegistrationScreen()
                isClickedRegisterText = true
            }, textDecoration = if (isClickedRegisterText) TextDecoration.Underline else TextDecoration.None)
            Spacer(modifier = Modifier.height(30.dp))
            Text("- - - - - - - - - - -  OR  - - - - - - - - - - -", color = Color.White, fontSize = 20.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(30.dp))
            ShowButtonForGoogleLogout()
        }


    }

}

@Composable
fun ShowInputUsername(usernameValue: String, addNewUsernameValue: (String) -> Unit){
    Column {
        Text(
            text = "Username",
            fontStyle = FontStyle.Italic,
            color = colorResource(R.color.lightGreen),
            textAlign = TextAlign.Start,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        TextField(
            value = usernameValue,
            onValueChange = addNewUsernameValue,
            leadingIcon = { Icon(imageVector = Icons.Sharp.Person, contentDescription = "Icono de Input") },
            shape = ShapeDefaults.ExtraSmall,
        )
    }
}

@Composable
fun ShowInputPassword(passwordValue: String, label: String, heightField: Dp = TextFieldDefaults.MinHeight, isError: Boolean, addNewPasswordValue: (String) -> Unit) {
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
                    Icon(painter = painterResource(if(canShowPassword) R.drawable.ic_visibility_off_24 else R.drawable.ic_visibility_24), contentDescription = "Icono para mostrar/ocultar la contraseña")
                }
            },
            leadingIcon = { Icon(painter = painterResource(R.drawable.ic_lock_24), contentDescription = "Icon password") },
            shape = ShapeDefaults.ExtraSmall,
            //Con el visualTransformation le digo que se transforme el texto a asterisco o no
            visualTransformation = if(canShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.height(heightField)
                .border(2.dp, if(isError) colorResource(R.color.red_error) else Color.Black, ShapeDefaults.ExtraSmall)
        )
    }
}

@Composable
fun ShowButtonForLoginOrRegister(text: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonColors(colorResource(R.color.lightGreen), Color.White, Color.LightGray, Color.LightGray),
        shape = ShapeDefaults.ExtraSmall
    ) {
        Text(
            text,
            modifier = Modifier.padding(vertical = 5.dp).width(150.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShowButtonForGoogleLogout() {
    TextButton(onClick = {  }, colors = ButtonColors(Color.White, colorResource(R.color.lightBlack), Color.LightGray, Color.LightGray)) {
        Text(
            "Login for Google  ",
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 10.dp)
        )
        Image(
            painter = painterResource(R.drawable.ic_google),
            contentDescription = "Google Icon",
            modifier = Modifier.padding(end = 10.dp)
        )

    }
}

fun validateUsername(username: String, changeErrorText: (String) -> Unit) {
    if(username.isEmpty()) {
        changeErrorText("Empty field")
    } else if() {
        
    }
}