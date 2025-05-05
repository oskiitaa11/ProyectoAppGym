package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.ui.screens.ShowNextQuestionForFirstQuestion
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.QuestionForModifierViewmodel
import com.google.android.play.integrity.internal.a
import com.google.common.math.LinearTransformation.horizontal
import com.google.firebase.database.collection.LLRBNode
import kotlinx.serialization.Serializable
import kotlin.collections.set

@Serializable
data class QuestionForModifierRoute(val answeredQuestion: String, val selectedResponses: List<String>, val responsesType: ResponsesType)

fun NavController.goToQuestionForModifier(answeredQuestion: String, selectedResponses: List<String>, responsesType: ResponsesType) {
    navigate(QuestionForModifierRoute(answeredQuestion, selectedResponses, responsesType))
}

fun NavGraphBuilder.questionForModifierDestination(backToEditProfile: () -> Unit) {
    composable<QuestionForModifierRoute> { navBackStackEntry ->
        val questionForModifierViewmodel: QuestionForModifierViewmodel =
            viewModel(navBackStackEntry) {
                QuestionForModifierViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).repositoryQuestions
                )
            }
        val questionForModifierRoute = navBackStackEntry.toRoute<QuestionForModifierRoute>()
        val questionForModifier = questionForModifierRoute.answeredQuestion
        val selectedResponses = questionForModifierRoute.selectedResponses
        val responsesType = questionForModifierRoute.responsesType
        val allResponsesDb =
            questionForModifierViewmodel.allQuestions.find { it.question == questionForModifier }?.responses?.toList() ?: emptyList()
        val isEqualFirstQuestion = questionForModifier == "¿Eres más de ejercicios de calistenia o gym?"
        val gymQuestion = if(isEqualFirstQuestion)
            questionForModifierViewmodel.allQuestions.find { "¿En qué tipos de ejercicios de Gym te enfocas más o te quieres enfocar?" == it.question }
        else null
        val calisthenicQuestion = if (isEqualFirstQuestion)
            questionForModifierViewmodel.allQuestions.find { "¿En qué tipos de ejercicios de Calistenia te enfocas más o te quieres enfocar?" == it.question }
        else null

        QuestionForModifierScreen(
            questionForModifier,
            allResponsesDb,
            selectedResponses,
            responsesType,
            backToEditProfile,
            gymQuestion,
            calisthenicQuestion,
            { question, newResponses ->
                questionForModifierViewmodel.updateResponsesOfQuestion(question, newResponses)
            }
        ) {
            question -> questionForModifierViewmodel.removeResponsesOfQuestion(question)
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun QuestionForModifierScreen(
    questionForModifier: String,
    allResponsesDb: List<String>,
    selectedResponsesDb: List<String>,
    responsesType: ResponsesType,
    backToEditProfile: () -> Unit,
    gymQuestion: Question?,
    calisthenicQuestion: Question?,
    changeCorrectedResponse: (String, List<String>) -> Unit,
    removeResponsesOfQuestion: (String) -> Unit
) {
    //var selectedResponse = remember { mutableStateListOf(*selectedResponsesDb.toTypedArray()) }
    var allResponses = remember { mutableStateMapOf(*getInitialPairsForResponses(selectedResponsesDb, allResponsesDb).toTypedArray()) }
    var isMultipleResponse = responsesType == ResponsesType.CHECKBOX
    var showDialogForQuestion by remember { mutableStateOf(false) }
    var allStringResponses = emptyList<String>()
    val updateResponses: () -> Unit = {
        allStringResponses = allResponses.filterValues { it }.keys.toList()

        if (allStringResponses.toSet() != allResponsesDb.toSet()) {
            if(allStringResponses.isNotEmpty())
                changeCorrectedResponse(questionForModifier, allStringResponses)
        }

        backToEditProfile()
    }
    val undoChanges: () -> Unit = {
        var selectedResponse = selectedResponsesDb[0]

        allResponses.forEach { response, isSelected ->
            allResponses[response] = false
        }
        allResponses[selectedResponse] = true
    }
    var selectedResponse = ""

    Scaffold(topBar = { ShowTopAppBarUpdateQuestion(backToEditProfile) }) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.lightBlack))
                .padding(innerpadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                questionForModifier,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(if(allResponsesDb.size > 4) 2 else 1),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                allResponses.forEach { (response, isSelected) ->
                    item {
                        ShowResponse(response, isSelected) { response, isSelected ->
                            if(isMultipleResponse)
                                allResponses[response] = isSelected
                            else
                                if(isSelected) {
                                    allResponses.forEach { response, isSelected ->
                                        allResponses[response] = false
                                    }
                                    allResponses[response] = true
                                } else {
                                    allResponses[response] = false
                                }
                        }
                    }
                }
            }

            ShowButtonsApply {
                if (gymQuestion == null && calisthenicQuestion == null) updateResponses()
                else {
                    allStringResponses = allResponses.filterValues { it }.keys.toList()
                    if (allStringResponses.toSet() != allResponsesDb.toSet())
                        if(allStringResponses.isNotEmpty()) showDialogForQuestion = true
                }

            }

            if(showDialogForQuestion) {
                selectedResponse = allResponses.filter { (response, isSelected) -> isSelected }.keys.toList()[0]
                ShowNextQuestionForFirstQuestion(
                    selectedResponse,
                    if(selectedResponse == "Gym") gymQuestion else null,
                    if(selectedResponse == "Calistenia") calisthenicQuestion else null,
                    { question, selectedResponses ->
                        if(selectedResponses.isEmpty()) undoChanges()
                            else
                                if(selectedResponse != "De los dos") removeResponsesOfQuestion(question)
                                changeCorrectedResponse(question, selectedResponses)
                                changeCorrectedResponse(questionForModifier, allStringResponses)
                                backToEditProfile()
                    },
                    undoChanges
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTopAppBarUpdateQuestion(backToEditProfile: () -> Unit) {
    TopAppBar(
        title = { Text("Update user data") },
        navigationIcon = {
            IconButton(backToEditProfile) {
                Icon(painter = painterResource(R.drawable.ic_arrow_back_24), contentDescription = "Icon back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent, navigationIconContentColor = Color.White, titleContentColor = Color.White)
    )
}

@Composable
fun ShowResponse(response: String, isCorrectedResponse: Boolean, changeCorrectedResponse: (String, Boolean) -> Unit) {
    var widthCard = if(response.length<=9) 150.dp else if(isCorrectedResponse) (response.length * 14).dp else (response.length * 14).dp

        Card(
            onClick = { changeCorrectedResponse(response, !isCorrectedResponse) },
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(if(isCorrectedResponse) R.color.lightGreen else R.color.black), contentColor = Color.White),
            modifier = Modifier
                .padding(10.dp)
                .width(widthCard)
                .wrapContentWidth(align = Alignment.CenterHorizontally)
                .sizeIn(minWidth = widthCard)
        ) {
            Text(response, color = Color.White, maxLines = 4, modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 15.dp))
        }
}

@Composable
fun ShowButtonsApply(updateCorrectedResponse: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            updateCorrectedResponse,
            shape = ShapeDefaults.Medium,
            colors = ButtonDefaults.textButtonColors(containerColor = colorResource(R.color.lightGreen)),
            modifier = Modifier.width(200.dp),
        ) {
            Text("Update", color = Color.White)
        }
    }
}

fun getInitialPairsForResponses(selectedResponsesDb: List<String>, allResponses: List<String>): List<Pair<String, Boolean>> {
    var pairsList = mutableListOf<Pair<String, Boolean>>()

    allResponses.forEach { response ->
        pairsList.add(response to selectedResponsesDb.any { it == response })
    }

    return pairsList
}

@Composable
fun ShowNextQuestionForFirstQuestion(selectedResponse: String, gymQuestion: Question?, calisthenicQuestion: Question?, updateQuestions: (String, List<String>) -> Unit, undoChanges: () -> Unit) {
    when(selectedResponse) {
        "Gym" -> ShowDialogQuestion(gymQuestion as Question, updateQuestions, undoChanges)
        "Calistenia" -> ShowDialogQuestion(calisthenicQuestion as Question, updateQuestions, undoChanges)
        else -> {
            if(gymQuestion == null) ShowDialogQuestion(calisthenicQuestion as Question, updateQuestions, undoChanges) else ShowDialogQuestion(gymQuestion, updateQuestions, undoChanges)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialogQuestion(question: Question, updateQuestions: (String, List<String>) -> Unit, undoChanges: () -> Unit) {
    var selectedResponses = remember { mutableStateListOf<String>() }

    AlertDialog(
        onDismissRequest = undoChanges,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    { updateQuestions(question.question, selectedResponses) },
                    colors = ButtonDefaults.textButtonColors(containerColor = colorResource(R.color.lightGreen), contentColor = Color.White),
                    modifier = Modifier.width(200.dp)
                ) {
                    Text("Update")
                }
            }
        },
        title = { Text(question.question) },
        text = {
            ShowResponsesDialog(
                question,
                { response -> response in selectedResponses },
                { response -> selectedResponses.add(response) },
                { response -> selectedResponses.remove(response) }
            )
        },
        containerColor = Color.White,
        titleContentColor = colorResource(R.color.lightBlack)
    )
}

@Composable
fun ShowResponsesDialog(question: Question, isSelectedResponse: (String) -> Boolean, addResponse: (String) -> Unit, removeResponse: (String) -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        question.responses.forEach { response ->
            ShowResponse(response, isSelectedResponse(response)) { response, isSelected ->
                if(isSelected) addResponse(response) else removeResponse(response)
            }
        }
    }
}
