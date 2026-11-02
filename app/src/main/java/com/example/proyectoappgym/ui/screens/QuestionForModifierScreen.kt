package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.RegistrationQuestionsRoute
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.entity.data.request.TrainingRoutineRequest
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.questions.BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.BUILD_MUSCLE_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.CALISTHENICS_QUESTION
import com.example.proyectoappgym.entity.questions.CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.GYM_QUESTION
import com.example.proyectoappgym.entity.questions.GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.HOW_MANY_DAYS_FOR_TRAINING
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.ResponsesType
import com.example.proyectoappgym.entity.questions.STRENGTH_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.TENS_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.TRAINING_TIME_QUESTION
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.example.proyectoappgym.ui.viewmodels.QuestionForModifierViewmodel
import com.example.proyectoappgym.ui.viewmodels.RegistrationQuestionsViewmodel
import com.google.android.gms.dynamic.IFragmentWrapper
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.serialization.Serializable
import kotlin.collections.getValue
import kotlin.collections.set
import kotlin.collections.setValue
import kotlin.collections.toMutableList

@Serializable
data class QuestionForModifierRoute(
    val answeredQuestion: String,
    val responsesType: ResponsesType
)

fun NavController.goToQuestionForModifier(
    answeredQuestion: String,
    responsesType: ResponsesType
) {
    navigate(QuestionForModifierRoute(answeredQuestion, responsesType))
}

fun NavGraphBuilder.questionForModifierDestination(backToEditProfile: () -> Unit, onErrorScreen: (String, Int) -> Unit) {
    composable<QuestionForModifierRoute> { navBackStackEntry ->
        val questionForModifierViewmodel: QuestionForModifierViewmodel = hiltViewModel()
        val registrationQuestionsViewmodel: RegistrationQuestionsViewmodel = hiltViewModel()
        val currentUser by questionForModifierViewmodel.currentUser.collectAsStateWithLifecycle()
        val questionForModifierRoute = navBackStackEntry.toRoute<QuestionForModifierRoute>()
        val questionForModifier = questionForModifierRoute.answeredQuestion
        val responsesType = questionForModifierRoute.responsesType
        val allResponsesDb =
            questionForModifierViewmodel.allQuestions.find { it.question == questionForModifier }?.responses?.toList()
                ?: emptyList()
        var newTrainingRoutines: List<TrainingRoutine>? = null
        val error by questionForModifierViewmodel.error.collectAsStateWithLifecycle(null)
        val context = LocalContext.current

        LaunchedEffect(questionForModifierViewmodel.error) {
            questionForModifierViewmodel.error.distinctUntilChanged().collect {
                if (it != null && it.errorType == ErrorType.ERROR_IN_ERROR_SCREEN)
                    onErrorScreen(it.error, it.idIcon as Int)
            }
        }

        LaunchedEffect(questionForModifierViewmodel.validationDone) {
            questionForModifierViewmodel.validationDone.distinctUntilChanged().collect {
                if (it) {
                    makeText(context, "User Updated", Toast.LENGTH_LONG).show()
                    backToEditProfile()
                }
            }
        }

        if (currentUser.name.isNotEmpty())
            QuestionForModifierScreen(
                questionForModifier,
                responsesType,
                error,
                backToEditProfile,
                QuestionsRegistration.allQuestions,
                currentUser.allQuestionsAnswered,
                { newAnsweredQuestions ->
                    if (changeRoutine(newAnsweredQuestions, currentUser.allQuestionsAnswered)) {
                        newTrainingRoutines = registrationQuestionsViewmodel.getTrainingRoutines(newAnsweredQuestions)
                    }
                    questionForModifierViewmodel.changeAllAnsweredQuestions(newAnsweredQuestions, newTrainingRoutines)
                }
            )
        else
            LoadingScreen()
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope", "SuspiciousIndentation")
@Composable
fun QuestionForModifierScreen(
    questionForModifier: String,
    responsesType: ResponsesType,
    errorWhileUserUpdates: ErrorForShowing?,
    backToEditProfile: () -> Unit,
    allInitialQuestions: List<Question>,
    actualAnsweredQuestions: Map<String, List<String>>,
    changeWeeklyRoutine: (Map<String, List<String>>) -> Unit
) {
    val allAnsweredQuestions = rememberSaveable {
        mutableMapOf(*getInitialPairsForResponses(allInitialQuestions, actualAnsweredQuestions, questionForModifier).toTypedArray())
    }
    val isMultipleResponse = responsesType == ResponsesType.CHECKBOX
    var showDialogForQuestion: Boolean? by rememberSaveable { mutableStateOf(null) }
    var allStringResponses = emptyList<String>()
    var actualResponsesInLoop: List<Pair<String, Boolean>>
    val undoChanges: () -> Unit = { //Dialogo para cuando se toque fuera del dialogo
        allAnsweredQuestions.forEach { (question, _) ->
            actualResponsesInLoop = actualAnsweredQuestions[question]?.map { Pair(it, it in (actualAnsweredQuestions[question] ?: emptyList())) } ?: emptyList()
            allAnsweredQuestions[question] = mutableStateMapOf(*actualResponsesInLoop.toTypedArray())
        }

        showDialogForQuestion = false
    }
    var questionInDialog: Question?
    var errorTextDialog by rememberSaveable { mutableStateOf("") }
    val actualResponsesForModifier = remember { allAnsweredQuestions[questionForModifier] }
    var responseForQuestionInDialog: SnapshotStateMap<String, Boolean>
    var finalAllAnsweredQuestions: Map<String, List<String>> = mapOf()
    var updateResponses by rememberSaveable { mutableStateOf(false) }
    val selectedResponsesDb = actualAnsweredQuestions[questionForModifier] ?: emptyList()
    var enabledUpdateButton by rememberSaveable { mutableStateOf(true) }
    var enabledUpdateButtonFromDialog by rememberSaveable { mutableStateOf(true) }
    val state = rememberScrollState()

    if (errorWhileUserUpdates != null) {
        ShowErrorDialogForLogging(errorWhileUserUpdates.error, errorWhileUserUpdates.idIcon as Int, backToEditProfile)
    }

    Scaffold(topBar = { ShowSimpleTopAppBar("Update user data", backToEditProfile) }) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.lightBlack))
                .verticalScroll(state)
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
            FlowRow(
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ) {
                actualResponsesForModifier?.forEach { (response, isSelected) ->
                    ShowResponse(response, isSelected) { response, isSelected ->
                        if (isMultipleResponse)
                            actualResponsesForModifier[response] = isSelected
                        else
                            if (isSelected) {
                                actualResponsesForModifier.forEach { (response, _) ->
                                    actualResponsesForModifier[response] = false
                                }
                                actualResponsesForModifier[response] = true
                            } else {
                                actualResponsesForModifier[response] = false
                            }
                    }
                }
            }

            ShowButtonsApply(enabledUpdateButton) {
                enabledUpdateButton = false
                allStringResponses = allAnsweredQuestions[questionForModifier]?.filter { (_, isSelected) -> isSelected }?.keys?.toList() ?: emptyList()
                if (allStringResponses.toSet() == selectedResponsesDb.toSet() || allStringResponses.isEmpty()) {
                    backToEditProfile()
                    return@ShowButtonsApply
                }
                checkingIfRemoveQuestion(
                    allStringResponses,
                    undoChanges,
                    { question ->
                        allAnsweredQuestions[question]?.keys?.toList()?.forEach { key ->
                            allAnsweredQuestions[question]?.set(key, false)
                        }
                    }
                )
                showDialogForQuestion = canShowDialogForNextQuestion(questionForModifier, selectedResponsesDb, allStringResponses)
            }

            if(showDialogForQuestion != null) {
                if (showDialogForQuestion == true) {
                    questionInDialog = getQuestionForShowInDialog(allStringResponses,
                        selectedResponsesDb, allInitialQuestions,
                        { question, response -> allInitialQuestions.first { it.question == question }.addResponse(response) })
                    responseForQuestionInDialog = remember { allAnsweredQuestions[questionInDialog!!.question]!! }

                    ShowDialogWithResponsesDb(
                        questionInDialog as Question,
                        allAnsweredQuestions[questionInDialog!!.question]!!.filter { it.value }.map { it.key },
                        if((questionInDialog?.question ?: "") != GOALS_QUESTION || questionForModifier == HOW_MANY_DAYS_FOR_TRAINING) errorTextDialog else "",
                        enabledUpdateButtonFromDialog,
                        { response ->
                            responseForQuestionInDialog[response] = true
                        },
                        { response ->
                            responseForQuestionInDialog[response] = false
                        },
                        undoChanges
                    ) {
                        enabledUpdateButtonFromDialog = false
                        updateResponses = true
                        showDialogForQuestion = null
                    }

                } else {
                    updateResponses = true
                    showDialogForQuestion = null
                    enabledUpdateButton = true
                }

            }

            if (updateResponses) {
                if (finalAllAnsweredQuestions != actualAnsweredQuestions) {
                    finalAllAnsweredQuestions = allAnsweredQuestions.mapValues { (_, responses) -> responses.filter { it.value }.keys.toList() }
                    changeWeeklyRoutine(finalAllAnsweredQuestions)
                }
                updateResponses = false
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ShowResponse(
    response: String,
    isCorrectedResponse: Boolean,
    changeCorrectedResponse: (String, Boolean) -> Unit
) {
    val widthCard =
        if (response.length <= 9) 150.dp else if (isCorrectedResponse) (response.length * 14).dp else (response.length * 14).dp

    Card(
        onClick = { changeCorrectedResponse(response, !isCorrectedResponse) },
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(if (isCorrectedResponse) R.color.lightGreen else R.color.black),
            contentColor = Color.White
        ),
        modifier = Modifier
            .padding(10.dp)
            .width(widthCard)
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .sizeIn(minWidth = widthCard)
    ) {
        Text(
            response,
            color = Color.White,
            maxLines = 4,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 15.dp)
        )
    }
}

@Composable
fun ShowButtonsApply(enabled: Boolean, updateCorrectedResponse: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        TextButton(
            onClick = updateCorrectedResponse,
            shape = ShapeDefaults.Medium,
            enabled = enabled,
            colors = ButtonDefaults.textButtonColors(
                containerColor = colorResource(R.color.lightGreen),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = colorResource(R.color.ultraLightGreen)
            ),
            modifier = Modifier.width(200.dp),
        ) {
            Text("Update", color = Color.White)
        }
    }
}

fun getInitialPairsForResponses(
    allQuestions: List<Question>,
    actualAnsweredQuestions: Map<String, List<String>>,
    questionForModifier: String?
): List<Pair<String, SnapshotStateMap<String, Boolean>>> {
    val newAnsweredQuestions: MutableList<Pair<String, SnapshotStateMap<String, Boolean>>> = mutableListOf()
    var actualResponses: List<Pair<String, Boolean>>

    allQuestions.forEach { question ->
        actualResponses = question.responses.associateWith { (actualAnsweredQuestions[question.question] ?: emptyList()).contains(it) }.toList()
        newAnsweredQuestions.add(question.question to mutableStateMapOf(*actualResponses.toTypedArray()))
    }

    if (questionForModifier == GOALS_QUESTION) {
        if (GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION in (actualAnsweredQuestions[CALISTHENICS_OR_GYM_QUESTION] ?: emptyList()))
            newAnsweredQuestions.first { it.first == GOALS_QUESTION }.second.remove(TENS_RESPONSE_FOR_GOALS_QUESTION)
    }
    return newAnsweredQuestions
}

@Composable
fun ShowDialogWithResponsesDb(
    question: Question,
    actualSelectedResponses: List<String>,
    errorText: String,
    enableButton: Boolean,
    addResponse: (String) -> Unit,
    removeResponse: (String) -> Unit,
    undoChanges: () -> Unit,
    updateQuestions: () -> Unit
) {
    AlertDialog(
        onDismissRequest = undoChanges,
        confirmButton = {
            ShowButtonsApply(enableButton, updateQuestions)
        },
        title = { Text(question.question) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ShowResponsesDialog(
                    question,
                    { response -> response in (actualSelectedResponses) },
                    addResponse,
                    removeResponse
                )
                if(errorText.isNotEmpty()) Text(errorText, color = Color.Red, fontSize = 15.sp)
            }
        },
        containerColor = Color.White,
        titleContentColor = colorResource(R.color.lightBlack)
    )
}

@Composable
fun ShowResponsesDialog(
    question: Question,
    isSelectedResponse: (String) -> Boolean,
    addResponse: (String) -> Unit,
    removeResponse: (String) -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        question.responses.forEach { response ->
            ShowResponse(response, isSelectedResponse(response)) { response, isSelected ->
                if (isSelected) addResponse(response) else removeResponse(response)
            }
        }
    }
}

fun getQuestionForShowInDialog(newSelectedResponses: List<String>, selectedResponsesDb: List<String>, allInitialQuestions: List<Question>, addResponse: (String, String) -> Unit): Question? {
    var questionInDialog: Question? = null

    if(selectedResponsesDb[0] != BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) {
        questionInDialog = if (newSelectedResponses[0] == GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) allInitialQuestions.first { it.question == GYM_QUESTION }
        else if (newSelectedResponses[0] == CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) allInitialQuestions.first { it.question == CALISTHENICS_QUESTION }
        else
            if (selectedResponsesDb[0] == GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) allInitialQuestions.first { it.question == CALISTHENICS_QUESTION }
            else allInitialQuestions.first { it.question == GYM_QUESTION }
    } else if (CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION in newSelectedResponses) {
        questionInDialog = allInitialQuestions.find { it.question == GOALS_QUESTION }
    }

    if (newSelectedResponses[0] == CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION)  addResponse(GOALS_QUESTION, TENS_RESPONSE_FOR_GOALS_QUESTION)

    return questionInDialog
}



fun  checkingIfRemoveQuestion(newResponses: List<String>, undoChanges: () -> Unit, removeResponsesOfQuestion: (String) -> Unit) {

    if (newResponses.isEmpty()) undoChanges()
    else {
        if (newResponses.any { it == GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION }) {
            removeResponsesOfQuestion(
                CALISTHENICS_QUESTION
            )
        } else if (newResponses.any { it == CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION }) {
            removeResponsesOfQuestion(
                GYM_QUESTION
            )
        }
    }
}

fun canShowDialogForNextQuestion(changedQuestion: String, selectedResponsesDb: List<String>, newSelectedResponses: List<String>): Boolean {
    return selectedResponsesDb.toSet() != newSelectedResponses.toSet() &&
            newSelectedResponses.isNotEmpty() && changedQuestion == CALISTHENICS_OR_GYM_QUESTION && (
            ((newSelectedResponses[0] == GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION || newSelectedResponses[0] == CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION)
                            && newSelectedResponses[0] != BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) || newSelectedResponses[0] == BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION)
}

fun changeRoutine(newAnsweredQuestions: Map<String, List<String>>, actualAnsweredQuestions: Map<String, List<String>>): Boolean =
    newAnsweredQuestions[CALISTHENICS_OR_GYM_QUESTION] != actualAnsweredQuestions[CALISTHENICS_OR_GYM_QUESTION] ||
            newAnsweredQuestions[GYM_QUESTION] != actualAnsweredQuestions[GYM_QUESTION] ||
            newAnsweredQuestions[GOALS_QUESTION] != actualAnsweredQuestions[GOALS_QUESTION] ||
            newAnsweredQuestions[HOW_MANY_DAYS_FOR_TRAINING] != actualAnsweredQuestions[HOW_MANY_DAYS_FOR_TRAINING] ||
            newAnsweredQuestions[TRAINING_TIME_QUESTION] != actualAnsweredQuestions[TRAINING_TIME_QUESTION]

