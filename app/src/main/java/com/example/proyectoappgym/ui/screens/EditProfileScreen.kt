package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.text.Layout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RippleDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.request.ImageRequest
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Avatars
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import com.google.common.math.Quantiles.scale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import okhttp3.Response
import java.nio.file.WatchEvent


@Serializable
object EditProfileRoute

fun NavController.goToEditProfileScreen() {
    navigate(EditProfileRoute)
}

fun NavGraphBuilder.editProfileDestination(backProfileScreen: () -> Unit, goToQuestionForModifierScreen: (String, List<String>, ResponsesType, Map<String, List<String>>) -> Unit) {
    composable<EditProfileRoute> { navBackStackEntry ->
        val editProfileViewmodel: EditProfileViewmodel = viewModel(navBackStackEntry) {
            EditProfileViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).repositoryQuestions
            )
        }
        val currentUser by editProfileViewmodel.currentUser.collectAsStateWithLifecycle()
        val allQuestions = editProfileViewmodel.allQuestions
        val showDialog by editProfileViewmodel.showDialog.collectAsStateWithLifecycle()
        val allStringQuestionUser = currentUser.allQuestionsAnswered.filter { (_, responses) -> responses.isNotEmpty() }.keys.toList()
        val allQuestionsFiltered = if(currentUser.allQuestionsAnswered.isNotEmpty())
            allQuestions.filter { question -> question.question in allStringQuestionUser }
        else emptyList()

        if(currentUser.username.isNotEmpty())
        EditProfileScreen(
            currentUser,
            allQuestionsFiltered,
            showDialog,
            backProfileScreen,
            { newName -> editProfileViewmodel.updateName(newName) },
            { newAvatar -> editProfileViewmodel.updateAvatarProfile(newAvatar) },
            { questionForModifier, selectedResponses, responseType -> goToQuestionForModifierScreen(questionForModifier, selectedResponses, responseType, currentUser.allQuestionsAnswered) },
            { editProfileViewmodel.updateShowDialog() }
        )
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditProfileScreen(currentUser: User, allQuestions: List<Question>, showDialog: Boolean, backProfileScreen: () -> Unit, updateName: (String) -> Unit, updateAvatarProfile: (Avatars) -> Unit, goToQuestionForModifierScreen: (String, List<String>, ResponsesType) -> Unit, changeShowDialog: () -> Unit) {
    var isEdited by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = currentUser.name,
                selection = TextRange(currentUser.name.length)
            )
        )
    }
    val scale = remember { Animatable(1f) }
    //var imageUri by remember { mutableStateOf<Uri?>(null) }
    /*val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        imageUri = uri
    }*/
    var avatarDb = Avatars.toAvatar(currentUser.idProfileAvatar)
    var currentAvatar by remember { mutableIntStateOf(avatarDb.idAvatarImage) }
    var showSheet by remember { mutableStateOf(false) }
    var widthTextField by remember { mutableIntStateOf(currentUser.name.length * 15) }


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

    if(showSheet) ShowBottomSheet({ showSheet = false }, currentAvatar, { newAvatar -> currentAvatar = newAvatar })

    Scaffold(
        topBar = {
            ShowTopAppBarEditProfileScreen {
                if(currentUser.name != textFieldValue.text) updateName(textFieldValue.text)
                if(currentAvatar != avatarDb.idAvatarImage) updateAvatarProfile(Avatars.fromId(currentAvatar))
                backProfileScreen()
            }
        }
    ) { innerpadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(colorResource(R.color.lightBlack))
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
                    }
                }
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            ShowProfileAvatar(currentAvatar) { showSheet = true }
            //ShowUpdateProfileAvatar(painter) { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 15.dp)
            ) {
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        if(it.text.length <= 14) textFieldValue = it else it.text.substring(14)
                        widthTextField = if(it.text.length <= 6 ) 80 else it.text.length * 15
                    },

                    enabled = isEdited,
                    textStyle = TextStyle(
                        textAlign = TextAlign.End,
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
                        }
                    ),
                    modifier = Modifier.focusRequester(focusRequester).width(widthTextField.dp),
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
                modifier = Modifier.fillMaxWidth().padding(start = 15.dp),
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.height(10.dp))
            ShowQuestionsUser(
                allQuestions,
                { question ->
                    currentUser.allQuestionsAnswered[question] ?: emptyList()
                },
                showDialog,
                goToQuestionForModifierScreen,
                changeShowDialog
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarEditProfileScreen(backProfileScreen: () -> Unit) {
    Column {
        TopAppBar(
            title = { Text("Edit profile") },
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

/*
@Composable
fun ShowUpdateProfileAvatar(painter: Any, showLauncherGallery: () -> Unit) {
    val imageModifier = Modifier.border(width = 3.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
        .size(80.dp)
        .clip(CircleShape)

    Box(modifier = Modifier.size(80.dp)) {
        if(painter is Uri) {
            AsyncImage(
                model = painter,
                contentDescription = "Profile avatar",
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(painterResource(painter as Int), "Profile avatar", modifier = imageModifier)
        }


        IconButton(
            onClick = showLauncherGallery,
            modifier = Modifier.align(Alignment.BottomEnd).size(32.dp)
        ) {
            Image(painterResource(R.drawable.ic_add_32), "Button for add", modifier = Modifier.fillMaxSize())
        }
    }
}*/

@Composable
fun ShowProfileAvatar(idPainter: Int, showLauncherBottomSheet: () -> Unit) {
    val imageModifier = Modifier.border(width = 3.dp, color = colorResource(R.color.lightGreen), shape = CircleShape)
        .size(80.dp)
        .clip(CircleShape)

    Box(modifier = Modifier.size(80.dp)) {
        Image(painterResource(idPainter), "Profile avatar", modifier = imageModifier,)

        IconButton(
            onClick = showLauncherBottomSheet,
            modifier = Modifier.align(Alignment.BottomEnd).size(32.dp)
        ) {
            Image(painterResource(R.drawable.ic_add_32), "Button for add", modifier = Modifier.fillMaxSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowBottomSheet(changeShowSheet: () -> Unit, currentAvatar: Int, changeAvatarProfile: (Int) -> Unit) {
    var sheetState = rememberModalBottomSheetState()
    var scope = rememberCoroutineScope()
    val avatars = listOf(
        R.drawable.avatar1,
        R.drawable.avatar2_1,
        R.drawable.avatar3,
    )

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
                        .border(2.dp, if (avatar == currentAvatar) Color.Blue else Color.Transparent, CircleShape)
                        .clickable {
                            changeAvatarProfile(avatar)
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
@Composable
fun ShowQuestionsUser(allQuestions: List<Question>, getSelectedResponsesOfQuestion: (String) -> List<String>, showDialog: Boolean, goToQuestionForModifierScreen: (String, List<String>, ResponsesType) -> Unit, changeShowDialog: () -> Unit) {
    LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
        itemsIndexed(items = allQuestions, key = { _, question -> question.id }) { id, question ->
            HorizontalDivider(thickness = 2.dp, color = Color.White.copy(alpha = 0.5f))
            ShowQuestionInEditProfile(
                question,
                getSelectedResponsesOfQuestion(question.question),
                showDialog,
                goToQuestionForModifierScreen,
                changeShowDialog
            )
        }
    }
}

@Composable
fun ShowQuestionInEditProfile(question: Question, selectedResponsesQuestion: List<String>, showDialog: Boolean, goToQuestionForModifierScreen: (String, List<String>, ResponsesType) -> Unit, changeShowDialog: () -> Unit) {
    var itemIsClicked by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(question.question, color = Color.White) },
        trailingContent = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_forward_ios_24),
                contentDescription = "Icon for show text",
                tint = Color.White
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent, headlineColor = Color.White, trailingIconColor = Color.White),
        modifier = Modifier.clickable {
            if(showDialog)
                itemIsClicked = true else
                goToQuestionForModifierScreen(question.question, selectedResponsesQuestion, question.responsesTypes)
        }
    )

    if(itemIsClicked) ShowWarningDialog(
        {
            changeShowDialog()
            goToQuestionForModifierScreen(question.question, selectedResponsesQuestion, question.responsesTypes)
        },
        { itemIsClicked = false },
        { itemIsClicked = false }
    )
}

@Composable
fun ShowWarningDialog(confirmAction: () -> Unit, cancelAction: () -> Unit, onDismissRequest: () -> Unit) {
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
        title = { Text("Try change the user data") },
        text = { Text("If you change your user data, your training routines will also change") }
    )
}

