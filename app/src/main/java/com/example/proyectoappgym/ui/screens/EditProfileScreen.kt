package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.text.Layout
import androidx.appcompat.widget.DialogTitle
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.data.request.DataUserRequest
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.users.Avatars
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.ResponsesType
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object EditProfileRoute

fun NavController.goToEditProfileScreen() {
    navigate(EditProfileRoute)
}

fun NavGraphBuilder.editProfileDestination(backProfileScreen: () -> Unit, goToQuestionForModifierScreen: (String, ResponsesType) -> Unit, onError: (String, Int) -> Unit) {
    composable<EditProfileRoute> { navBackStackEntry ->
        val editProfileViewmodel: EditProfileViewmodel = hiltViewModel()
        val currentUser by editProfileViewmodel.currentUser.collectAsStateWithLifecycle()
        val allQuestions = editProfileViewmodel.allQuestions
        val showDialog by editProfileViewmodel.showDialog.collectAsStateWithLifecycle()
        val allStringQuestionUser = currentUser.allQuestionsAnswered.filter { (_, responses) -> responses.isNotEmpty() }.keys.toList()
        val allQuestionsFiltered = if(currentUser.allQuestionsAnswered.isNotEmpty())
            allQuestions.filter { question -> question.question in allStringQuestionUser }
        else emptyList()
        val error by editProfileViewmodel.error.collectAsStateWithLifecycle(null)

        LaunchedEffect(editProfileViewmodel.error) {
            editProfileViewmodel.error.distinctUntilChanged().collect {
                if (it != null && it.errorType == ErrorType.ERROR_IN_ERROR_SCREEN)
                    onError(it.error, it.idIcon as Int)
            }
        }

        if(currentUser.username.isNotEmpty())
            EditProfileScreen(
                currentUser,
                allQuestionsFiltered,
                error,
                showDialog,
                backProfileScreen,
                { name -> editProfileViewmodel.updateCurrentUser(name, Avatars.toAvatar(currentUser.idProfileAvatar)) },
                { avatar -> editProfileViewmodel.updateCurrentUser(currentUser.name, avatar) },
                { questionForModifier, responseType -> goToQuestionForModifierScreen(questionForModifier, responseType) },
                { editProfileViewmodel.updateShowDialog() }
            )
        else
            LoadingScreen()
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditProfileScreen(currentUser: User, allQuestions: List<Question>, error: ErrorForShowing?, showDialog: Boolean, backProfileScreen: () -> Unit, updateNameUser: (String) -> Unit, updateAvatar: (Avatars) -> Unit, goToQuestionForModifierScreen: (String, ResponsesType) -> Unit, changeShowDialog: () -> Unit) {
    var isEdited by rememberSaveable { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldValue by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue(
                text = currentUser.name,
                selection = TextRange(currentUser.name.length)
            )
        )
    }
    val scale = remember { Animatable(1f) }
    var showSheet by rememberSaveable { mutableStateOf(false) }
    var errorForDialog by rememberSaveable { mutableStateOf(error) }

    if (errorForDialog != null)
        ShowErrorDialogForLogging(errorForDialog!!.error, errorForDialog?.idIcon as Int, { errorForDialog = null })

    ApplyAnimationForWhenOnClick(isEdited, scale)

    if (isEdited) {
        /*Si el isEdited es true tira un LaunchedEffect, para cuando ya se haya recompuesto la ui
        el BasicTextField, ya activado, reciba el foco*/
        LaunchedEffect(Unit) {
            //Antes de recibir el foco lo ponemos al final del texto
            textFieldValue = textFieldValue.copy(
                selection = TextRange(textFieldValue.text.length)
            )
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    if(showSheet) ShowBottomSheet(
        { showSheet = false },
        currentUser.idProfileAvatar,
        { newAvatar -> if (currentUser.idProfileAvatar != newAvatar.idAvatar) updateAvatar(newAvatar) }
    )

    Scaffold(
        topBar = {
            ShowSimpleTopAppBar("Edit profile", backProfileScreen)
        }
    ) { innerpadding ->
        LazyColumn(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(colorResource(R.color.lightBlack))
                .padding(
                    top = innerpadding.calculateTopPadding(),
                    start = innerpadding.calculateStartPadding(LayoutDirection.Ltr),
                    end = innerpadding.calculateEndPadding(LayoutDirection.Rtl)
                )
                .fillMaxSize()
                .pointerInput(Unit) {//Como se toque a fuera del input se quitara el foco y el teclado desaparece
                    detectTapGestures {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        isEdited = false
                        validateFieldNameUser(
                            textFieldValue.text,
                            currentUser.name,
                            { updateNameUser(textFieldValue.text) })
                    }
                }
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                ShowProfileAvatar(currentUser.idProfileAvatar) { showSheet = true }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 35.dp)
                ) {
                    TextField(
                        value = textFieldValue,
                        onValueChange = {
                            if(it.text.length <= 14) textFieldValue = it else it.text.take(14)
                        },

                        enabled = isEdited,
                        textStyle = TextStyle(
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            color = Color.White
                        ),
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                keyboardController?.hide()
                                isEdited = false
                                validateFieldNameUser(textFieldValue.text, currentUser.name, { updateNameUser(textFieldValue.text) })
                            }
                        ),
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .width(180.dp),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        )
                    )
                    IconButton(
                        { isEdited = true },
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_edit_24),
                            contentDescription = "Edit name",
                            tint = colorResource(R.color.lightGreen)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "User data for make routines",
                    fontSize = 20.sp,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(10.dp))

            }
            showQuestionsUser(
                allQuestions,
                showDialog,
                goToQuestionForModifierScreen,
                changeShowDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSimpleTopAppBar(title: String,backProfileScreen: () -> Unit) {
    Column {
        TopAppBar(
            title = { Text(title) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(R.color.lightBlack), navigationIconContentColor = Color.White, titleContentColor = Color.White, actionIconContentColor = Color.White),
            navigationIcon = {
                IconButton(backProfileScreen) {
                    Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Go back icon")
                }
            }
        )
        HorizontalDivider(thickness = 2.dp, color = Color.White)
    }
}

@Composable
fun ApplyAnimationForWhenOnClick(isEdited: Boolean, scale: Animatable<Float, AnimationVector1D>) {
    LaunchedEffect(isEdited) {
        if (isEdited) {
            scale.animateTo(
                targetValue = 1.4f,
                animationSpec = tween(durationMillis = 1000)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 1000)
            )
        }
    }
}

@Composable
fun ShowProfileAvatar(idPainter: Int, showLauncherBottomSheet: () -> Unit) {
    val imageModifier = Modifier
        .border(width = 3.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
        .size(80.dp)
        .clip(CircleShape)
    val avatar = Avatars.toAvatar(idPainter)

    Box(modifier = Modifier.size(80.dp)) {
        Image(painterResource(avatar.idAvatarImage), "Profile avatar", modifier = imageModifier)

        IconButton(
            onClick = showLauncherBottomSheet,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
        ) {
            Image(painterResource(R.drawable.ic_add_32), "Button for add", modifier = Modifier.fillMaxSize())
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(changeShowSheet: () -> Unit, currentAvatar: Int, changeAvatarProfile: (Avatars) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val avatars = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2_1,
        R.drawable.avatar3,
    )
    val currentIdImg by mutableIntStateOf(Avatars.toAvatar(currentAvatar).idAvatarImage)

    ModalBottomSheet(
        onDismissRequest = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) {
                    changeShowSheet()
                }
            }
        },
        sheetState = sheetState
    ) {
        Text(
            "Select your avatar",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(avatars) { avatar ->
                Image(
                    painter = painterResource(avatar),
                    contentDescription = "Avatar opción",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .border(
                            2.dp,
                            if (avatar == currentIdImg) Color.Blue else Color.Transparent,
                            CircleShape
                        )
                        .clickable {
                            changeAvatarProfile(Avatars.fromId(avatar))
                            scope.launch {
                                sheetState.hide()
                                changeShowSheet()
                            }
                        }
                )
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
fun LazyListScope.showQuestionsUser(allQuestions: List<Question>, showDialog: Boolean, goToQuestionForModifierScreen: (String, ResponsesType) -> Unit, changeShowDialog: () -> Unit) {
    itemsIndexed(items = allQuestions, key = { _, question -> question.id }) { _, question ->
        HorizontalDivider(thickness = 2.dp, color = Color.White.copy(alpha = 0.5f))
        ShowItemWithDialog(
            text = question.question,
            textForDialog = "If you change your user data, your training routines will also change",
            titleForDialog = "Try change the user data",
            trailingIdIcon = R.drawable.ic_arrow_forward_ios_24,
            showDialog = showDialog,
            onClickConfirmButton = {
                changeShowDialog()
                goToQuestionForModifierScreen(question.question, question.responsesTypes)
            }
        )
    }
}

@Composable
fun ShowItemWithDialog(text: String, colorText: Color = Color.White, background: Color = Color.Transparent, textForDialog: String = "", titleForDialog: String = "", leadingIdIcon: Int? = null, trailingIdIcon: Int? = null, showDialog: Boolean, onClickConfirmButton: () -> Unit) {
    var itemIsClicked by rememberSaveable { mutableStateOf(false) }

    ListItem(
        headlineContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIdIcon != null) {
                    Icon(painterResource(leadingIdIcon), "reference icon", tint = colorText, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Text(text, color = colorText)
            }
        },
        trailingContent = {
            if (trailingIdIcon != null)
                Icon(
                    painter = painterResource(trailingIdIcon),
                    contentDescription = "Icon for show text",
                    modifier = Modifier.size(20.dp),
                    tint = colorText
                )
        },
        colors = ListItemDefaults.colors(containerColor = background, headlineColor = colorText, trailingIconColor = colorText),
        modifier = Modifier.clickable {
            if(showDialog)
                itemIsClicked = true
            else
                onClickConfirmButton()
        }
    )

    if(itemIsClicked) ShowWarningDialog(
        titleForDialog,
        textForDialog,
        {
            onClickConfirmButton()
        },
        { itemIsClicked = false },
        { itemIsClicked = false }
    )
}

@Composable
fun ShowWarningDialog(title: String, text: String, confirmAction: () -> Unit, cancelAction: () -> Unit, onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton =  {
            TextButton(confirmAction) {
                Text("Accept")
            }
        },
        dismissButton =  {
            TextButton(cancelAction) {
                Text("Cancel")
            }
        },
        title = { Text(title) },
        text = { Text(text) }
    )
}

fun validateFieldNameUser(newName: String, actualName: String, updateNameUser: () -> Unit) {
    if (newName != actualName && newName.isNotEmpty()) updateNameUser()
}

