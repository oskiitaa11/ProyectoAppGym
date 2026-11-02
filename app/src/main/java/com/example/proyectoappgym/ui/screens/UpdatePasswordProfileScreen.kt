package com.example.proyectoappgym.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.ui.viewmodels.UpdatePasswordProfileViewmodel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.Serializable

@Serializable
object UpdatePasswordProfileRoute

fun NavController.goToUpdatePasswordProfileScreen() {
    navigate(UpdatePasswordProfileRoute)
}

fun NavGraphBuilder.updatePasswordProfileDestination(onBack: () -> Unit) {
    composable<UpdatePasswordProfileRoute> { navBackStackEntry ->
        val updatePasswordProfileViewmodel: UpdatePasswordProfileViewmodel = hiltViewModel()
        val currentUser by updatePasswordProfileViewmodel.currentUser.collectAsStateWithLifecycle()
        val error by updatePasswordProfileViewmodel.error.collectAsStateWithLifecycle()
        val context = LocalContext.current
        val isInvalidCredentials by updatePasswordProfileViewmodel.isInvalidCredentials.collectAsStateWithLifecycle()

        LaunchedEffect(updatePasswordProfileViewmodel.validationDone) {
            updatePasswordProfileViewmodel.validationDone.distinctUntilChanged().collect {
                if (it) {
                    makeText(context, "User updates", Toast.LENGTH_LONG).show()
                    onBack()
                }
            }
        }

        if (currentUser.username.isNotEmpty())
            UpdateFieldPasswordScreen(
                error,
                isInvalidCredentials,
                { updatePasswordProfileViewmodel.setErrorToNull() },
                { updatePasswordProfileViewmodel.setIsInvalidCredentialToFalse() },
                { currentPassword, newValue ->
                    updatePasswordProfileViewmodel.updatePassword( currentPassword, newValue)
                },
                onBack
            )
        else
            LoadingScreen()
    }
}

@Composable
fun UpdateFieldPasswordScreen(errorWhileUpdating: ErrorForShowing?, isInvalidCredentials: Boolean, setErrorToNull: () -> Unit, setIsInvalidPasswordToFalse: () -> Unit, updatePassword: (String, String) -> Unit, onBack: () -> Unit) {
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

    LaunchedEffect(errorWhileUpdating) {
        if (errorWhileUpdating != null) {
            showErrorDialog = true
            disableButton = false
        }
    }

    LaunchedEffect(isInvalidCredentials) {
        if (isInvalidCredentials) {
            disableButton = false
            fieldValidatedProperly = false
        }
    }

    if (showErrorDialog) ShowErrorDialogForLogging(
        errorWhileUpdating!!.error,
        errorWhileUpdating.idIcon as Int,
        {
            setErrorToNull()
            showErrorDialog = false
        }
    )

    Scaffold(
        topBar = { ShowSimpleTopAppBar("Update password", onBack) },
        floatingActionButton = {
            if (fieldValidatedProperly && newFieldValidatedProperly)
                Box(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                    contentAlignment = if (isLandscape) Alignment.BottomEnd
                    else Alignment.TopEnd
                ) {
                    ShowUpdateButton("Update profile", !disableButton) {
                        disableButton = true
                        validateField(
                            newValue,
                            value,
                            "password",
                            {
                                validatePassword(newValue, { newError -> errorForNewValue = newError })
                                newFieldValidatedProperly = errorForNewValue.isEmpty()
                                validatePassword(value, { newError -> errorForValue = newError })
                                fieldValidatedProperly = errorForValue.isEmpty()
                            },
                            { newError -> errorForNewValue = newError }
                        )

                        if (fieldValidatedProperly && newFieldValidatedProperly)
                            updatePassword(value, newValue)
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
            ShowInputPassword(
                passwordValue = value,
                label = "Password",
                error = if (isInvalidCredentials) "Invalid credentials, please re-enter the correct password" else errorForValue,
                addNewPasswordValue = { newText -> value = newText },
                validatePasswordField = {
                    if (isInvalidCredentials) setIsInvalidPasswordToFalse()
                    validateField(
                        newValue,
                        value,
                        "password",
                        {
                            validatePassword(value, { newError -> errorForValue = newError })
                            fieldValidatedProperly = errorForValue.isEmpty()
                        },
                        { newError -> errorForValue = newError }
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
            ShowInputPassword(
                passwordValue = newValue,
                label = "New password",
                error = errorForNewValue,
                addNewPasswordValue = { newText -> newValue = newText },
                validatePasswordField = {
                    validateField(
                        newValue,
                        value,
                        "password",
                        {
                            validatePassword(newValue, { newError -> errorForNewValue = newError })
                            newFieldValidatedProperly = errorForNewValue.isEmpty()
                        },
                        { newError -> errorForNewValue = newError }
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))
        }
    }

}