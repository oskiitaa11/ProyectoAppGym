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
import androidx.compose.runtime.currentComposer
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.App
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.retrofit.entity.Routines
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.entity.TypeExercise
import com.example.proyectoappgym.ui.screens.ShowNextQuestion
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.QuestionForModifierViewmodel
import com.google.android.play.integrity.internal.a
import com.google.common.math.LinearTransformation.horizontal
import com.google.firebase.database.collection.LLRBNode
import kotlinx.serialization.Serializable
import kotlin.collections.set

@Serializable
data class QuestionForModifierRoute(
    val answeredQuestion: String,
    val selectedResponses: List<String>,
    val responsesType: ResponsesType
)

fun NavController.goToQuestionForModifier(
    answeredQuestion: String,
    selectedResponses: List<String>,
    responsesType: ResponsesType
) {
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
        val currentUser by questionForModifierViewmodel.currentUser.collectAsStateWithLifecycle()
        val hasChangeRoutine by questionForModifierViewmodel.isChangeRoutine.collectAsStateWithLifecycle()
        val questionForModifierRoute = navBackStackEntry.toRoute<QuestionForModifierRoute>()
        val questionForModifier = questionForModifierRoute.answeredQuestion
        val selectedResponses = questionForModifierRoute.selectedResponses
        val responsesType = questionForModifierRoute.responsesType
        val allResponsesDb =
            questionForModifierViewmodel.allQuestions.find { it.question == questionForModifier }?.responses?.toList()
                ?: emptyList()
        val isEqualFirstQuestion =
            questionForModifier == "Are you more into calisthenics or gym workouts?"
        val selectedInitialResponse =
            if (isEqualFirstQuestion) questionForModifierRoute.selectedResponses[0] else null

        if (currentUser.name.isNotEmpty())
            QuestionForModifierScreen(
                questionForModifier,
                allResponsesDb,
                selectedResponses,
                selectedInitialResponse,
                responsesType,
                backToEditProfile,
                QuestionsRegistration.allQuestions,
                currentUser.allQuestionsAnswered,
                hasChangeRoutine,
                { newAnsweredQuestions ->
                    questionForModifierViewmodel.changeWeeklyRoutine(newAnsweredQuestions)
                },
                { question, newResponses ->
                    questionForModifierViewmodel.updateResponsesOfQuestion(question, newResponses)
                }
            ) { question ->
                questionForModifierViewmodel.removeResponsesOfQuestion(question)
            }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope", "SuspiciousIndentation")
@Composable
fun QuestionForModifierScreen(
    questionForModifier: String,
    allResponsesDb: List<String>,
    selectedResponsesDb: List<String>,
    selectedInitialResponse: String?,
    responsesType: ResponsesType,
    backToEditProfile: () -> Unit,
    allInitialQuestions: List<Question>,
    actualAnsweredQuestions: Map<String, List<String>>,
    hasChangeRoutines: Boolean?,
    changeWeeklyRoutine: (Map<String, List<String>>) -> Unit,
    changeCorrectedResponse: (String, List<String>) -> Unit,
    removeResponsesOfQuestion: (String) -> Unit
) {
    var allResponses = remember {
        mutableStateMapOf(
            *getInitialPairsForResponses(
                selectedResponsesDb,
                allResponsesDb
            ).toTypedArray()
        )
    } //Variable donde se guardan las respuesta y si estan seleccionadas
    var isMultipleResponse = responsesType == ResponsesType.CHECKBOX
    var showDialogForQuestion by remember { mutableStateOf(false) } //Variable para mostrar dialogo
    var allStringResponses = emptyList<String>() //Variable para guardar las respuestas en cadena
    val updateResponses: () -> Unit = {//Funcion para actualizar las respuestas en la base de datos
        allStringResponses = allResponses.filterValues { it }.keys.toList()

        if (allStringResponses.toSet() != selectedResponsesDb.toSet()) {
            if (allStringResponses.isNotEmpty())
                changeCorrectedResponse(questionForModifier, allStringResponses)
                changeWeeklyRoutine(actualAnsweredQuestions.toMutableMap().apply { set(questionForModifier, allStringResponses) })
        }
        backToEditProfile()
    }
    val undoChanges: () -> Unit = { //Dialogo para cuando se toque fuera del dialogo
        var selectedResponse = selectedResponsesDb[0]

        allResponses.forEach { response, isSelected ->
            allResponses[response] = false
        }
        allResponses[selectedResponse] = true

        showDialogForQuestion = false
    }
    var selectedResponse = ""
    var questionInDialog: Question? = null
    var functionAccordingQuestion: (String, List<String>) -> Unit = { question, selectedResponses ->  }
    var numberTypeExercises = (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
    var numberDaysOfWeek = actualAnsweredQuestions["Which days of the week can/do you want to train?"]?.size ?: 0
    var showDialogForQuestionDaysOfWeek by remember { mutableStateOf(false) }
    var questionDialog = remember { "" }
    var newSelectedResponsesDialog = remember { emptyList<String>() }

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
                            if (isMultipleResponse)
                                allResponses[response] = isSelected
                            else
                                if (isSelected) {
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
                allStringResponses = allResponses.filter { (_, isSelected) -> isSelected }.keys.toList()
                questionInDialog = getQuestionForShowInDialog(questionForModifier, allStringResponses, numberDaysOfWeek, numberTypeExercises, selectedResponsesDb, allInitialQuestions)
                showDialogForQuestion = canShowDialogForNextQuestion(questionForModifier, selectedResponsesDb, allStringResponses, actualAnsweredQuestions)
                if(showDialogForQuestion) {
                    functionAccordingQuestion = getFunctionAccordingQuestion(
                        questionForModifier,
                        allStringResponses,
                        selectedResponsesDb,
                        undoChanges,
                        removeResponsesOfQuestion,
                        { question, newSelectedResponses ->
                            changeCorrectedResponse(questionForModifier, allStringResponses)
                            changeCorrectedResponse(question, newSelectedResponses)
                        },
                        { question, newSelectedResponses ->
                            actualAnsweredQuestions.toMutableMap().apply {
                                set(questionForModifier, allStringResponses)
                                set(question, newSelectedResponses)
                            }
                        },
                        backToEditProfile,
                        { question, newSelectedResponses ->
                            when(question) {
                                "What types of gym exercises do you focus on or want to focus on?" -> newSelectedResponses.size + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0) > numberDaysOfWeek
                                "What types of calisthenics exercises do you focus on or want to focus on?" -> newSelectedResponses.size + (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) > numberDaysOfWeek
                                else -> false
                            }
                        },
                        {
                            showDialogForQuestion = false
                            showDialogForQuestionDaysOfWeek = true
                        },
                        { question, selectedResponse ->
                            questionDialog = question
                            newSelectedResponsesDialog = selectedResponse
                        }
                    )
                }

            }

            if (showDialogForQuestion) {
                selectedResponse =
                    allResponses.filter { (_, isSelected) -> isSelected }.keys.toList()[0]
                ShowDialogQuestion(questionInDialog as Question, undoChanges, functionAccordingQuestion)
                /*ShowNextQuestion(
                    selectedResponse,
                    selectedInitialResponse ?: "",
                    questionInDialog,
                    functionAccordingQuestion,
                    *//*{ question, selectedResponses ->
                        if (selectedResponses.isEmpty()) undoChanges()
                        else {
                            if (selectedResponse == "Gym") removeResponsesOfQuestion(
                                calisthenicQuestion?.question ?: ""
                            )
                            else if (selectedResponse == "Calisthenics") removeResponsesOfQuestion(
                                gymQuestion?.question ?: ""
                            )

                            if (selectedInitialResponse != "Both") changeCorrectedResponse(
                                question,
                                selectedResponses
                            )
                            if (allStringResponses.isNotEmpty()) {
                                changeCorrectedResponse(questionForModifier, allStringResponses)

                                changeWeeklyRoutine(
                                    actualAnsweredQuestions.toMutableMap().apply {
                                        set(questionForModifier, allStringResponses)
                                        set(question, selectedResponses)
                                    }
                                )
                                backToEditProfile()
                            }
                        }

                    }*//*
                    undoChanges
                )*/
            } else if(showDialogForQuestionDaysOfWeek) {
                ShowDialogQuestion(allInitialQuestions.find { it.question == "Which days of the week can/do you want to train?" } as Question, undoChanges) { questionDaysOfWeek, daysOfWeek ->
                    var actualNumberTypeExercises = getActualNumberTypeExercises(questionInDialog?.question ?: "", daysOfWeek, actualAnsweredQuestions) +
                            getActualNumberTypeExercises(questionForModifier, allStringResponses, actualAnsweredQuestions)

                    if(daysOfWeek.isNotEmpty()) {
                        if(actualNumberTypeExercises > numberDaysOfWeek) {
                            changeCorrectedResponse(questionInDialog?.question ?: "", newSelectedResponsesDialog)
                            changeCorrectedResponse(questionForModifier, allStringResponses)
                            changeCorrectedResponse(questionDaysOfWeek, daysOfWeek)
                            changeWeeklyRoutine(actualAnsweredQuestions.toMutableMap().apply { 
                                set(questionDaysOfWeek, daysOfWeek)
                                set(questionInDialog?.question ?: "", newSelectedResponsesDialog)
                                set(questionForModifier, allStringResponses)
                            })
                        }
                    } else undoChanges()
                }
            } else updateResponses()
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
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back_24),
                    contentDescription = "Icon back"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            navigationIconContentColor = Color.White,
            titleContentColor = Color.White
        )
    )
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ShowResponse(
    response: String,
    isCorrectedResponse: Boolean,
    changeCorrectedResponse: (String, Boolean) -> Unit
) {
    var widthCard =
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
fun ShowButtonsApply(updateCorrectedResponse: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
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

fun getInitialPairsForResponses(
    selectedResponsesDb: List<String>,
    allResponses: List<String>
): List<Pair<String, Boolean>> {
    var pairsList = mutableListOf<Pair<String, Boolean>>()

    allResponses.forEach { response ->
        pairsList.add(response to selectedResponsesDb.any { it == response })
    }

    return pairsList
}

@Composable
fun ShowNextQuestion(
    selectedResponse: String,
    selectedInitialResponse: String,
    question: Question?,
    updateQuestions: (String, List<String>) -> Unit,
    undoChanges: () -> Unit
) {
    /*var isSelectedInitialResponse = selectedInitialResponse == "Both"

    if (isSelectedInitialResponse) {
        updateQuestions(if(selectedResponse == "Gym") gymQuestion?.question ?: "" else calisthenicQuestion?.question ?: "", listOf(""))
        return
    }

    when (selectedResponse) {
        "Gym" -> ShowDialogQuestion(gymQuestion as Question, updateQuestions, undoChanges)
        "Calisthenics" -> ShowDialogQuestion(
            calisthenicQuestion as Question,
            updateQuestions,
            undoChanges
        )

        else -> {
            if (selectedInitialResponse == "Gym") ShowDialogQuestion(
                calisthenicQuestion as Question,
                updateQuestions,
                undoChanges
            )
            else ShowDialogQuestion(gymQuestion as Question, updateQuestions, undoChanges)
        }
    }*/
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDialogQuestion(
    question: Question,
    undoChanges: () -> Unit,
    updateQuestions: (String, List<String>) -> Unit
) {
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
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = colorResource(R.color.lightGreen),
                        contentColor = Color.White
                    ),
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
fun ShowResponsesDialog(
    question: Question,
    isSelectedResponse: (String) -> Boolean,
    addResponse: (String) -> Unit,
    removeResponse: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        question.responses.forEach { response ->
            ShowResponse(response, isSelectedResponse(response)) { response, isSelected ->
                if (isSelected) addResponse(response) else removeResponse(response)
            }
        }
    }
}

fun getQuestionForShowInDialog(question: String, newSelectedResponses: List<String>, daysOfWeek: Int, typeExercises: Int, selectedResponsesDb: List<String>, allInitialQuestions: List<Question>): Question? =

    when (question) {
        "Are you more into calisthenics or gym workouts?" -> {
            if(newSelectedResponses[0] == "Gym") allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" }
            else if(newSelectedResponses[0] == "Calisthenics") allInitialQuestions.find { it.question == "What types of calisthenics exercises do you focus on or want to focus on?" }
            else
                if(selectedResponsesDb[0] == "Calisthenics") allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" }
                else allInitialQuestions.find { it.question == "What types of calisthenics exercises do you focus on or want to focus on?" }
        }
        "What are your goals?" -> if(newSelectedResponses.any { it == "Build more muscle" }) allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" } else null
        "What types of calisthenics exercises do you focus on or want to focus on?", "What types of gym exercises do you focus on or want to focus on?" -> if(typeExercises > daysOfWeek) allInitialQuestions.find { it.question == "Which days of the week can/do you want to train?" } else null
        else -> null
    }



fun getFunctionAccordingQuestion(questionForModifier: String, newResponses: List<String>, selectedResponsesDb: List<String>, undoChanges: () -> Unit, removeResponsesOfQuestion: (String) -> Unit, changeCorrectedResponse: (String, List<String>) -> Unit, changeWeeklyRoutine: (String, List<String>) -> Unit, backToEditProfile: () -> Unit, typeExercisesIsMoreThanDaysOfWeek: (String, List<String>) -> Boolean, showDialogForDaysOfWeek: () -> Unit, saveQuestionAndNewResponses: (String, List<String>) -> Unit): (String, List<String>) -> Unit {

    return when(questionForModifier) {
        "Are you more into calisthenics or gym workouts?" -> { question, selectedResponses ->
            var typeExercisesIsMoreThanDaysOfWeek = typeExercisesIsMoreThanDaysOfWeek(question, selectedResponses)

            if (selectedResponses.isEmpty()) undoChanges()
            else {
                if (selectedResponsesDb[0] == "Gym") removeResponsesOfQuestion(
                    "What types of calisthenics exercises do you focus on or want to focus on?"
                )
                else if (selectedResponsesDb[0] == "Calisthenics") removeResponsesOfQuestion(
                    "What types of gym exercises do you focus on or want to focus on?"
                )

                if (selectedResponsesDb[0] != "Both") changeCorrectedResponse(
                    question,
                    selectedResponses
                )
                if (newResponses.isNotEmpty()) {
                    if(typeExercisesIsMoreThanDaysOfWeek) {
                        changeCorrectedResponse(question, newResponses)
                        changeWeeklyRoutine(question, newResponses)
                        backToEditProfile()
                    } else {
                        saveQuestionAndNewResponses(question, selectedResponses)
                        showDialogForDaysOfWeek()
                    }

                }
            }
        }
        "What are your goals?" -> { question, selectedResponses ->

        }
        "Which days of the week can/do you want to train?" -> { question, selectedResponses ->

        }
        "What types of calisthenics exercises do you focus on or want to focus on?" -> { question, selectedResponses ->

        }
        "What types of gym exercises do you focus on or want to focus on?" -> { question, selectedResponses ->

        }
        else -> { question, selectedResponses -> }
    }

}

fun canShowDialogForNextQuestion(changedQuestion: String, selectedResponsesDb: List<String>, newSelectedResponses: List<String>, allQuestionsDb: Map<String, List<String>>): Boolean {

    return if(selectedResponsesDb.toSet() != newSelectedResponses.toSet()) {
        if(newSelectedResponses.isNotEmpty()){
            when(changedQuestion) {
                "Are you more into calisthenics or gym workouts?" -> selectedResponsesDb[0] != "Both"
                "What are your goals?" -> newSelectedResponses.any { it == "Build more muscle" }
                "What types of calisthenics exercises do you focus on or want to focus on?" -> newSelectedResponses.size + (allQuestionsDb["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) > (allQuestionsDb["Which days of the week can/do you want to train?"]?.count() ?: 0)
                "What types of gym exercises do you focus on or want to focus on?" -> newSelectedResponses.size + (allQuestionsDb["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) > (allQuestionsDb["Which days of the week can/do you want to train?"]?.count() ?: 0)
                else -> false
            }
        } else false
    } else false

}

fun getActualNumberTypeExercises(question: String, selectedResponses: List<String>, actualAnsweredQuestions: Map<String, List<String>>): Int =
   when(question) {
       "What types of gym exercises do you focus on or want to focus on?" -> selectedResponses.size + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
       "What types of calisthenics exercises do you focus on or want to focus on?" -> selectedResponses.size + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
       else -> 0
   }
