package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.exoplayer.mediacodec.MediaCodecAdapter
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.viewmodels.UpdateEmailProfileViewmodel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.Serializable

@Serializable
object UpdateEmailProfileRoute

fun NavController.goToUpdateEmailProfileScreen() {
    navigate(UpdateEmailProfileRoute)
}

fun NavGraphBuilder.updateEmailProfileDestination(onBack: () -> Unit) {
    composable<UpdateEmailProfileRoute> { navBackStackEntry ->
        val updateEmailProfileViewmodel: UpdateEmailProfileViewmodel = hiltViewModel()
        val currentUser by updateEmailProfileViewmodel.currentUser.collectAsStateWithLifecycle()
        val newValueExists by updateEmailProfileViewmodel.newValueExists.collectAsStateWithLifecycle()
        val error by updateEmailProfileViewmodel.error.collectAsStateWithLifecycle()
        val context = LocalContext.current

        LaunchedEffect(updateEmailProfileViewmodel.validationDone) {
            updateEmailProfileViewmodel.validationDone.distinctUntilChanged().collect {
                if (it) {
                    makeText(context, "User updates", Toast.LENGTH_LONG).show()
                    onBack()
                }
            }
        }

        if (currentUser.username.isNotEmpty())
            UpdateFieldEmailScreen(
                currentUser,
                newValueExists,
                error,
                { updateEmailProfileViewmodel.setErrorToNull() },
                { updateEmailProfileViewmodel.setEmailExistsToNull() },
                { newValue ->
                    updateEmailProfileViewmodel.updateEmail(newValue)
                },
                onBack
            )
        else
            LoadingScreen()
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ConfigurationScreenWidthHeight")
@Composable
fun UpdateFieldEmailScreen(user: User, newValueExists: Boolean?, errorWhileUpdating: ErrorForShowing?, setErrorWhileUpdatingToNull: () -> Unit, setValueExistsToNull: () -> Unit, updateEmail: (String) -> Unit, onBack: () -> Unit) {
    var value by rememberSaveable { mutableStateOf("") }
    var errorForValue by rememberSaveable { mutableStateOf("") }
    var errorForNewValue by rememberSaveable { mutableStateOf("") }
    var newValue by rememberSaveable { mutableStateOf("") }
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }
    var fieldValidatedProperly by rememberSaveable { mutableStateOf(false) }
    var newFieldValidatedProperly by rememberSaveable { mutableStateOf(false) }
    var disableButton by rememberSaveable { mutableStateOf(false) }
    val stateScroll = rememberScrollState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val focus = LocalFocusManager.current

    if (newValueExists != null) {
        if (newValueExists) {
            errorForNewValue = "Email already exists, please insert other value again"
            newFieldValidatedProperly = false
        } else newFieldValidatedProperly = true
        setValueExistsToNull()
        disableButton = false
    }

    LaunchedEffect(errorWhileUpdating) {
        if (errorWhileUpdating != null) {
            showErrorDialog = true
            setValueExistsToNull()
            disableButton = false
        }

    }

    if (showErrorDialog) ShowErrorDialogForLogging(
        errorWhileUpdating!!.error,
        errorWhileUpdating.idIcon as Int,
        {
            setErrorWhileUpdatingToNull()
            showErrorDialog = false
        }
    )

    Scaffold(
        topBar = { ShowSimpleTopAppBar("Update email", onBack) },
        floatingActionButton = {
            if (fieldValidatedProperly && newFieldValidatedProperly)
                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(if (!isLandscape) 0.5f else 1f),
                    contentAlignment = if (!isLandscape) Alignment.TopEnd else Alignment.BottomEnd
                ) {
                    ShowUpdateButton("Update profile", !disableButton) {
                        disableButton = true
                        validateField(
                            newValue,
                            value,
                            "email",
                            {
                                validateEmailLogin(value, { newError -> errorForValue = newError })
                                fieldValidatedProperly = errorForValue.isEmpty()
                                validateEmail(
                                    newValue,
                                    user.username.trim() == newValue.split(Regex("@"))[0],
                                    { newError -> errorForNewValue = newError }
                                )
                                newFieldValidatedProperly = errorForNewValue.isEmpty()
                            },
                            { newError -> errorForValue = newError }
                        )

                        if (newFieldValidatedProperly && fieldValidatedProperly)
                            updateEmail(newValue)
                        else disableButton = false
                    }
                }
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(stateScroll)
                .background(color = colorResource(R.color.lightBlack))
                .padding(innerpadding)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focus.clearFocus()
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ShowInputNormal(
                value,
                "Email",
                R.drawable.ic_email_24,
                error = errorForValue,
                addNewFieldValue = { newText -> value = newText },
                validateUsernameField = {
                    validateField(
                        newValue,
                        value,
                        "email",
                        {
                            validateEmailLogin(value, { newError -> errorForValue = newError })
                            if (errorForValue.isEmpty() && value != user.email)
                                errorForValue = "Email incorrect"
                            fieldValidatedProperly = errorForValue.isEmpty()
                        },
                        { newError -> errorForValue = newError }
                    )
                }
            )
            //ShowErrorText(errorForValue, 10.dp)
            Spacer(modifier = Modifier.height(15.dp))
            ShowInputNormal(
                fieldValue = newValue,
                label = "New email",
                idIcon = R.drawable.ic_email_24,
                error = errorForNewValue,
                addNewFieldValue = { newText -> newValue = newText },
                validateUsernameField = {
                    validateField(
                        newValue,
                        value,
                        "email",
                        {
                            validateEmail(
                                newValue,
                                user.username.trim() == newValue.split(Regex("@"))[0],
                                { newError -> errorForNewValue = newError }
                            )
                            newFieldValidatedProperly = errorForNewValue.isEmpty()
                        },
                        { newError -> errorForNewValue = newError }
                    )
                }
            )

            //ShowErrorText(errorForNewValue, 10.dp)
            Spacer(modifier = Modifier.height(15.dp))
        }
    }

}

@Composable
fun ShowUpdateButton(textLabel: String, enabled: Boolean, updateProfile: () -> Unit) {
    FloatingActionButton(
        { if (enabled) updateProfile() },
        containerColor = colorResource(if (enabled) R.color.lightGreen else R.color.ultraLightGreen),
        contentColor = colorResource(R.color.white),
        shape = ShapeDefaults.Medium,
        modifier = Modifier
            .padding(end = 20.dp, bottom = 20.dp)
            .width(140.dp)
            .height(40.dp)
    ) {
        Text(textLabel, textAlign = TextAlign.Center)
    }
}

fun validateField(newValue: String, value: String, fieldForChanging: String, validations: () -> Unit, setError: (String) -> Unit) {
    setError("")
    if (newValue != value) {
        validations()
    } else setError("The actual $fieldForChanging can't be equal to the new $fieldForChanging")
}