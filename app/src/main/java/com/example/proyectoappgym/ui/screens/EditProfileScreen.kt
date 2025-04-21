package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.ImeOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
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
import com.example.proyectoappgym.entity.User
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.ProfileViewmodel
import kotlinx.serialization.Serializable


@Serializable
object EditProfileRoute

fun NavController.goToEditProfileScreen() {
    navigate(EditProfileRoute)
}

fun NavGraphBuilder.editProfileDestination(backProfileScreen: () -> Unit) {
    composable<EditProfileRoute> { navBackStackEntry ->
        val editProfileViewmodel: EditProfileViewmodel = viewModel(navBackStackEntry) {
            EditProfileViewmodel(
                (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
            )
        }
        val currentUser by editProfileViewmodel.currentUser.collectAsStateWithLifecycle()

        if(currentUser.username.isNotEmpty())
        EditProfileScreen(currentUser, backProfileScreen, { newName -> editProfileViewmodel.updateName(newName) }, { uriNewAvatar -> editProfileViewmodel.updateAvatarProfile(uriNewAvatar.toUri(), currentUser) } )
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun EditProfileScreen(currentUser: User, backProfileScreen: () -> Unit, updateName: (String) -> Unit, updateAvatarProfile: (String) -> Unit) {
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        imageUri = uri
    }
    val painter: Any = if(imageUri != null) imageUri as Uri else if(currentUser.profileAvatar.isNotEmpty()) currentUser.profileAvatar else R.drawable.predetermined_avatar

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

    Scaffold(
        topBar = {
            ShowTopAppBarEditProfileScreen {
                if(currentUser.name != textFieldValue.text) updateName(textFieldValue.text)
                if(imageUri != null) updateAvatarProfile(imageUri.toString())
                backProfileScreen()
            }
        }
    ) { innerpadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(colorResource(R.color.lightBlack))
                .padding(innerpadding)
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
            ShowUpdateProfileAvatar(painter) { galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            Spacer(modifier = Modifier.height(5.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = textFieldValue,
                    onValueChange = { if(it.text.length <= 14) textFieldValue = it else it.text.substring(14) },
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
                    modifier = Modifier.focusRequester(focusRequester).width(180.dp),
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
            Spacer(modifier = Modifier.height(10.dp))
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
}