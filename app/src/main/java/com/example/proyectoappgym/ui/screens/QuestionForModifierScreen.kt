package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.ResponsesType
import com.example.proyectoappgym.ui.viewmodels.QuestionForModifierViewmodel
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
    }
    var isMultipleResponse = responsesType == ResponsesType.CHECKBOX
    var showDialogForQuestion: Boolean? by remember { mutableStateOf(null) }
    var allStringResponses = emptyList<String>()
    var newAnsweredQuestions = remember { actualAnsweredQuestions.toMutableMap() }
    var questionForRemove: String? = remember { null }
    val updateResponses: () -> Unit = {
        allStringResponses = allResponses.filterValues { it }.keys.toList()

        if (allStringResponses.toSet() != selectedResponsesDb.toSet()) {
            if (allStringResponses.isNotEmpty())
                changeCorrectedResponse(questionForModifier, allStringResponses)
                if(questionForRemove != null){
                    if(questionForModifier == "What are your goals?" && (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) > 0) {
                        changeCorrectedResponse(questionForRemove ?: "", newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.toMutableList().apply {
                            this!!.remove("Machine exercises")
                            if((newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) <= 1) add("Weightlifting exercises")
                        } ?: emptyList())
                    } else if(questionForModifier == "Are you more into calisthenics or gym workouts?" && questionForRemove == "What types of calisthenics exercises do you focus on or want to focus on?") {
                        var responsesForGym = newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList()

                        if("Machine exercises" !in responsesForGym) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { remove("Build more muscle") })
                        else if("Machine exercises" in responsesForGym) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") })
                    } else removeResponsesOfQuestion(questionForRemove ?: "")
                }
                if(questionForModifier == "What types of gym exercises do you focus on or want to focus on?") {
                    var responsesForGoals = newAnsweredQuestions["What are your goals?"] ?: emptyList()

                    if("Machine exercises" in allStringResponses && "Build more muscle" !in responsesForGoals) {
                        newAnsweredQuestions["What are your goals?"] = responsesForGoals.toMutableList().apply { add("Build more muscle") }
                        changeCorrectedResponse("What are your goals?", newAnsweredQuestions["What are your goals?"] ?: emptyList())
                    }
                }
                changeWeeklyRoutine(newAnsweredQuestions.toMutableMap().apply {
                    set(questionForModifier, allStringResponses)
                    if (questionForRemove != null) {
                        if (questionForModifier == "What are your goals?") {
                            set(
                                "What types of gym exercises do you focus on or want to focus on?",
                                newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.toMutableList()
                                    .apply {
                                        this!!.remove("Machine exercises")
                                        if ((newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size
                                                ?: 0) <= 1
                                        ) add("Weightlifting exercises")
                                    } ?: emptyList())
                        } else if (questionForModifier == "Are you more into calisthenics or gym workouts?" && questionForRemove == "What types of calisthenics exercises do you focus on or want to focus on?") {
                            var responsesForGym = newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList()

                            if ("Machine exercises" !in responsesForGym) changeCorrectedResponse(
                                "What are your goals?",
                                (actualAnsweredQuestions["What are your goals?"]
                                    ?: emptyList()).toMutableList()
                                    .apply { remove("Build more muscle") })
                            else if ("Machine exercises" in responsesForGym) changeCorrectedResponse(
                                "What are your goals?",
                                (actualAnsweredQuestions["What are your goals?"]
                                    ?: emptyList()).toMutableList()
                                    .apply { add("Build more muscle") })
                        } else set(questionForRemove ?: "", emptyList())
                    }
                }
                )
        }
        backToEditProfile()
    }
    val undoChanges: () -> Unit = { //Dialogo para cuando se toque fuera del dialogo
        allResponses.forEach { response, isSelected ->
            allResponses[response] = false
        }

        selectedResponsesDb.forEach {
            allResponses[it] = true
        }


        showDialogForQuestion = false
    }
    var selectedResponse = ""
    var questionInDialog: Question? = remember { null }
    var functionAccordingQuestion: (String, List<String>, List<String>) -> Unit = { question, selectedResponses, selectedResponsesDb ->  }
    var numberTypeExercises = (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
    var numberDaysOfWeek = actualAnsweredQuestions["Which days of the week can/do you want to train?"]?.size ?: 0
    var showDialogForQuestionDaysOfWeek: Boolean? by remember { mutableStateOf(null) }
    var selectedDaysOfWeek = remember { mutableStateListOf(*(actualAnsweredQuestions["Which days of the week can/do you want to train?"] ?: emptyList()).toTypedArray()) }
    var errorTextDialog by remember { mutableStateOf("") }
    var selectedResponsesQuestionDialog = remember {
        mutableStateListOf(*(newAnsweredQuestions[if(questionForModifier == "What are your goals?") "What types of gym exercises do you focus on or want to focus on?" else "Which days of the week can/do you want to train?" ] ?: emptyList()).toTypedArray())
    }
    var allSelectedTypesExercises = remember {
        mutableStateListOf(
            *(
                    (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList())
                            + (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"] ?: emptyList())
                    ).toTypedArray()
        )
    }

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
                if (allStringResponses.toSet() == selectedResponsesDb.toSet()) {
                    return@ShowButtonsApply
                }

                if(allStringResponses.isEmpty()){
                    backToEditProfile()
                    return@ShowButtonsApply
                }

                questionInDialog = getQuestionForShowInDialog(questionForModifier, allStringResponses, numberDaysOfWeek, selectedResponsesDb, allInitialQuestions,
                    "Gym" in (actualAnsweredQuestions["Are you more into calisthenics or gym workouts?"] ?: emptyList()) ||
                            "Both" in (actualAnsweredQuestions["Are you more into calisthenics or gym workouts?"] ?: emptyList()),
                "Both" in (actualAnsweredQuestions["Are you more into calisthenics or gym workouts?"] ?: emptyList()) ||
                        "Calisthenics" in (actualAnsweredQuestions["Are you more into calisthenics or gym workouts?"] ?: emptyList())
                ) { question, responsesGym, responsesCalisthenics ->
                    (responsesGym?.size
                        ?: (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size
                            ?: 0)) +
                            (responsesCalisthenics?.size
                                ?: (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size
                                    ?: 0)) > numberDaysOfWeek
                }
                if(questionInDialog == null) questionForRemove = getQuestionForRemove(questionForModifier, allStringResponses, numberDaysOfWeek, numberTypeExercises, selectedResponsesDb, allInitialQuestions)
                showDialogForQuestion = canShowDialogForNextQuestion(questionForModifier, selectedResponsesDb, allStringResponses, numberTypeExercises, { question, responsesGym, responsesCalisthenics ->
                    (responsesGym?.size ?: (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0)) +
                            (responsesCalisthenics?.size ?: (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)) > numberDaysOfWeek
                })

                if(showDialogForQuestion == true) {
                    functionAccordingQuestion = getFunctionAccordingQuestion(
                        questionForModifier,
                        allStringResponses,
                        undoChanges,
                        { question ->
                            newAnsweredQuestions[question] = emptyList<String>()
                            questionForRemove = question
                            //removeResponsesOfQuestion(question)
                        },
                        { question, newSelectedResponses ->
                            changeCorrectedResponse(questionForModifier, allStringResponses)
                            changeCorrectedResponse(question, newSelectedResponses)
                            if(questionForRemove != null) changeCorrectedResponse(questionForRemove ?: "", emptyList())
                            if((questionInDialog?.question ?: "") == "What types of gym exercises do you focus on or want to focus on?") {
                                if("Machine exercises" !in newSelectedResponses) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { remove("Build more muscle") })
                                else if("Machine exercises" in newSelectedResponses) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") })
                            } else if((questionInDialog?.question ?: "") == "Which days of the week can/do you want to train?") {
                                if("Machine exercises" in allStringResponses && "Machine exercises" !in (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList())) {
                                    newAnsweredQuestions["What are your goals?"] = (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") }
                                    changeCorrectedResponse("What are your goals?", (newAnsweredQuestions["What are your goals?"] ?: emptyList()))
                                }

                            }

                            if(questionForModifier == "Which days of the week can/do you want to train?") {
                                newAnsweredQuestions.toMutableMap()[question] = newSelectedResponses
                            }

                        },
                        { question, newSelectedResponses ->
                            changeWeeklyRoutine(newAnsweredQuestions.toMutableMap().apply {
                                set(questionForModifier, allStringResponses)
                                set(question, newSelectedResponses)
                                if((questionInDialog?.question ?: "") == "What types of gym exercises do you focus on or want to focus on?")
                                    if("Machine exercises" !in newSelectedResponses) set("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { remove("Build more muscle") })
                                    else if("Machine exercises" in newSelectedResponses) set("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") })
                            })
                        },
                        backToEditProfile,
                        { question, responsesGym, responsesCalisthenics ->
                            (responsesGym?.size ?: (actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0)) +
                                    (responsesCalisthenics?.size ?: (actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)) > numberDaysOfWeek
                        },
                        {
                            showDialogForQuestion = false
                            showDialogForQuestionDaysOfWeek = true
                        },
                        { question, selectedResponse ->
                            newAnsweredQuestions[question] = selectedResponse
                        },
                        { errorText -> errorTextDialog = errorText },
                        {
                            allStringResponses.size + if(questionForModifier == "What types of gym exercises do you focus on or want to focus on?") actualAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0
                            else if(questionForModifier == "What types of calisthenics exercises do you focus on or want to focus on?") actualAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0 else 0
                        }
                    )
                }

            }

            if(showDialogForQuestion != null || showDialogForQuestionDaysOfWeek != null) {
                if (showDialogForQuestion == true) {
                    selectedResponse = allResponses.filter { (_, isSelected) -> isSelected }.keys.toList()[0]
                    if(questionForModifier != "Are you more into calisthenics or gym workouts?") ShowDialogWithResponsesDb(
                        questionInDialog as Question,
                        if(questionForModifier == "Which days of the week can/do you want to train?") allSelectedTypesExercises else selectedResponsesQuestionDialog,
                        if((questionInDialog?.question ?: "") != "What are your goals?" || questionForModifier == "Which days of the week can/do you want to train?") errorTextDialog else "",
                        { response ->
                            if(questionForModifier != "Which days of the week can/do you want to train?") selectedResponsesQuestionDialog.add(response) else allSelectedTypesExercises.add(response)
                        },
                        { response ->
                            if(questionForModifier != "Which days of the week can/do you want to train?") selectedResponsesQuestionDialog.remove(response) else allSelectedTypesExercises.remove(response)
                        },
                        undoChanges
                    ) { question, responses ->
                        functionAccordingQuestion(question, responses, actualAnsweredQuestions[question] ?: emptyList())
                        showDialogForQuestion = null
                    }
                        else ShowDialogQuestion(
                        question = questionInDialog as Question,
                        undoChanges = {
                            undoChanges()
                            showDialogForQuestion = null
                        }
                    ) { question, responses ->
                        functionAccordingQuestion(question, responses, actualAnsweredQuestions[question] ?: emptyList())
                        showDialogForQuestion = null
                    }

                } else if(showDialogForQuestionDaysOfWeek == true) {
                    ShowDialogWithResponsesDb(allInitialQuestions.find { it.question == "Which days of the week can/do you want to train?" } as Question, selectedDaysOfWeek, errorTextDialog, { response -> selectedDaysOfWeek.add(response) }, { response -> selectedDaysOfWeek.remove(response) }, undoChanges) { questionDaysOfWeek, daysOfWeek ->
                        var actualNumberTypeExercises = getActualNumberTypeExercises(newAnsweredQuestions)

                        if(daysOfWeek.isNotEmpty()) {
                            if(actualNumberTypeExercises <= daysOfWeek.size) {
                                var responsesForGym = newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"] ?: emptyList()
                                errorTextDialog = ""
                                var c = newAnsweredQuestions[questionInDialog?.question ?: ""] ?: emptyList<String>()

                                if(questionForRemove != null) removeResponsesOfQuestion(questionForRemove ?: "")
                                changeCorrectedResponse(questionInDialog?.question ?: "", c)
                                if((questionInDialog?.question ?: "") == "What types of gym exercises do you focus on or want to focus on?")
                                    if("Machine exercises" !in responsesForGym) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { remove("Build more muscle") })
                                    else if("Machine exercises" in responsesForGym) changeCorrectedResponse("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") })
                                changeCorrectedResponse(questionForModifier, allStringResponses)
                                changeCorrectedResponse(questionDaysOfWeek, daysOfWeek)
                                changeWeeklyRoutine(newAnsweredQuestions.toMutableMap().apply {
                                    set(questionDaysOfWeek, daysOfWeek)
                                    set(questionForModifier, allStringResponses)
                                    if((questionInDialog?.question ?: "") == "What types of gym exercises do you focus on or want to focus on?")
                                        if("Machine exercises" !in responsesForGym) set("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { remove("Build more muscle") })
                                        else if("Machine exercises" in responsesForGym) set("What are your goals?", (actualAnsweredQuestions["What are your goals?"] ?: emptyList()).toMutableList().apply { add("Build more muscle") })
                                })
                                backToEditProfile()
                                showDialogForQuestionDaysOfWeek = null
                            } else {
                                var  numberTypeExercises = (newAnsweredQuestions["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) + (newAnsweredQuestions["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
                                errorTextDialog = "Choose at least $numberTypeExercises days of training"
                            }
                        } else {
                            undoChanges()
                            showDialogForQuestionDaysOfWeek = null
                        }

                    }
                } else {
                    updateResponses()
                    showDialogForQuestion = null
                }
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
            onClick = updateCorrectedResponse,
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
                    {  updateQuestions(question.question, selectedResponses) },
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
fun ShowDialogWithResponsesDb(
    question: Question,
    actualSelectedResponses: List<String>,
    errorText: String,
    addResponse: (String) -> Unit,
    removeResponse: (String) -> Unit,
    undoChanges: () -> Unit,
    updateQuestions: (String, List<String>) -> Unit
) {
    AlertDialog(
        onDismissRequest = undoChanges,
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    { updateQuestions(question.question, actualSelectedResponses) },
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(if(question.responses.size > 4) 2 else 1),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center
    ) {
        question.responses.forEach { response ->
            item {
                ShowResponse(response, isSelectedResponse(response)) { response, isSelected ->
                    if (isSelected) addResponse(response) else removeResponse(response)
                }
            }
        }
    }
}



fun getQuestionForShowInDialog(question: String, newSelectedResponses: List<String>, numberTypeExerciseForDaysQuestion: Int, selectedResponsesDb: List<String>, allInitialQuestions: List<Question>, doGym: Boolean, doCalisthenics: Boolean, typeExercisesIsMoreThanDaysOfWeek: (String, List<String>?, List<String>?) -> Boolean): Question? {
    var questionInDialog: Question? = null
    val responsesInDialog: MutableList<String> = mutableListOf()

    when (question) {
        "Are you more into calisthenics or gym workouts?" -> {
            if(selectedResponsesDb[0] != "Both") {
                if (newSelectedResponses[0] == "Gym") questionInDialog = allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" }
                else if (newSelectedResponses[0] == "Calisthenics") questionInDialog = allInitialQuestions.find { it.question == "What types of calisthenics exercises do you focus on or want to focus on?" }
                else
                    if (selectedResponsesDb[0] == "Calisthenics") questionInDialog = allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" }
                    else questionInDialog = allInitialQuestions.find { it.question == "What types of calisthenics exercises do you focus on or want to focus on?" }
            }
        }
        "What are your goals?" -> if(doGym)
            if ("Build more muscle" in newSelectedResponses && "Build more muscle" !in selectedResponsesDb) questionInDialog = allInitialQuestions.find { it.question == "What types of gym exercises do you focus on or want to focus on?" } else questionInDialog = null
        "What types of gym exercises do you focus on or want to focus on?" -> if(typeExercisesIsMoreThanDaysOfWeek(question, newSelectedResponses, null)) questionInDialog = allInitialQuestions.find { it.question == "Which days of the week can/do you want to train?" } else questionInDialog = null
        "What types of calisthenics exercises do you focus on or want to focus on?" -> if(typeExercisesIsMoreThanDaysOfWeek(question, null, newSelectedResponses)) questionInDialog = allInitialQuestions.find { it.question == "Which days of the week can/do you want to train?" } else questionInDialog = null
        "Which days of the week can/do you want to train?" -> {
            if(numberTypeExerciseForDaysQuestion > newSelectedResponses.size){
                if(doGym) {
                    responsesInDialog.add("Machine exercises")
                    responsesInDialog.add("Weightlifting exercises")
                }

                if(doCalisthenics) {
                    responsesInDialog.add("Tension exercises")
                    responsesInDialog.add("Basic exercises")
                }

                questionInDialog = Question(34, "You remove a type exercise", ResponsesType.CHECKBOX, *responsesInDialog.toTypedArray())
            }
        }
        else -> questionInDialog = null
    }
    return questionInDialog
}



fun  getFunctionAccordingQuestion(questionForModifier: String, newResponses: List<String>, undoChanges: () -> Unit, removeResponsesOfQuestion: (String) -> Unit, changeCorrectedResponse: (String, List<String>) -> Unit, changeWeeklyRoutine: (String, List<String>) -> Unit, backToEditProfile: () -> Unit, typeExercisesIsMoreThanDaysOfWeek: (String, List<String>?, List<String>?) -> Boolean, showDialogForDaysOfWeek: () -> Unit, saveQuestionAndNewResponses: (String, List<String>) -> Unit, addErrorText: (String) -> Unit, getNumberTypeExercises: () -> Int): (String, List<String>, List<String>) -> Unit {

    return when(questionForModifier) {
        "Are you more into calisthenics or gym workouts?" -> { question, selectedResponses, _ ->
            var typeExercisesIsMoreThanDaysOfWeek: Boolean
            var gymResponses: List<String>? = if(selectedResponses.contains("Weightlifting exercises") || selectedResponses.contains("Machine exercises")) selectedResponses else null
            var calisthenicsResponses: List<String>? = if(selectedResponses.contains("Tension exercises") || selectedResponses.contains("Basic exercises")) selectedResponses else null

            if (selectedResponses.isEmpty()) undoChanges()
            else {
                if (newResponses[0] == "Gym") {
                    gymResponses = selectedResponses
                    calisthenicsResponses = emptyList()
                    removeResponsesOfQuestion(
                        "What types of calisthenics exercises do you focus on or want to focus on?"
                    )
                }
                else if (newResponses[0] == "Calisthenics"){
                    calisthenicsResponses = selectedResponses
                    gymResponses = emptyList()
                    removeResponsesOfQuestion(
                        "What types of gym exercises do you focus on or want to focus on?"
                    )
                }

                if (selectedResponses.isNotEmpty()) {
                    typeExercisesIsMoreThanDaysOfWeek = typeExercisesIsMoreThanDaysOfWeek(question, gymResponses, calisthenicsResponses)
                    if(typeExercisesIsMoreThanDaysOfWeek) {
                        saveQuestionAndNewResponses(question, selectedResponses)
                        showDialogForDaysOfWeek()
                    } else {
                        changeCorrectedResponse(question, selectedResponses)
                        changeWeeklyRoutine(question, selectedResponses)
                        backToEditProfile()
                    }

                }
            }
        }
        "What are your goals?" -> { question, selectedResponses, selectedResponsesDb2 ->
            var typeExercisesIsMoreThanDaysOfWeek: Boolean

            if(selectedResponses.isNotEmpty()) {
                if(selectedResponsesDb2.toSet() != selectedResponses.toSet()) {
                    typeExercisesIsMoreThanDaysOfWeek = typeExercisesIsMoreThanDaysOfWeek(question, emptyList(), newResponses)
                    if(typeExercisesIsMoreThanDaysOfWeek) {
                        saveQuestionAndNewResponses(question, selectedResponses)
                        showDialogForDaysOfWeek()
                    } else {
                        changeCorrectedResponse(question, selectedResponses)
                        changeWeeklyRoutine(question, selectedResponses)
                        backToEditProfile()
                    }
                }
            } else undoChanges()

        }
        "Which days of the week can/do you want to train?" -> { question, selectedResponses, selectedResponsesDb2 ->
            var gymResponses = selectedResponses.filter { it == "Tension exercises" || it == "Basic exercises" }
            var calisthenicsResponses = selectedResponses.filter { it == "Machine exercises" || it == "Weightlifting exercises" }

            if(selectedResponses.isNotEmpty()) {
                if(selectedResponses.toSet() != selectedResponsesDb2.toSet()) {
                    if(newResponses.size < selectedResponses.size) {
                        addErrorText("You must at least ${selectedResponses.size} type exercises")
                    } else {
                        changeCorrectedResponse("What types of calisthenics exercises do you focus on or want to focus on?", calisthenicsResponses)
                        changeCorrectedResponse("What types of gym exercises do you focus on or want to focus on?", gymResponses)
                        changeWeeklyRoutine("What types of gym exercises do you focus on or want to focus on?", gymResponses)
                        backToEditProfile()
                    }
                }
            } else undoChanges()

        }
        "What types of calisthenics exercises do you focus on or want to focus on?", "What types of gym exercises do you focus on or want to focus on?" -> { question, selectedResponses, selectedResponsesDb2 ->
            var numberTypeExercises = getNumberTypeExercises()

            if(selectedResponses.isNotEmpty()) {
                if(selectedResponsesDb2.toSet() != selectedResponses.toSet()) {
                    if(numberTypeExercises > selectedResponses.size) {
                        addErrorText("Choose at least $numberTypeExercises days of week")
                    } else {
                        changeCorrectedResponse(question, selectedResponses)
                        changeWeeklyRoutine(question, selectedResponses)
                        backToEditProfile()
                    }
                }
            } else undoChanges()

        }
        else -> { question, selectedResponses, selectedResponsesDB2 -> }
    }

}

fun canShowDialogForNextQuestion(changedQuestion: String, selectedResponsesDb: List<String>, newSelectedResponses: List<String>, numberTypeExerciseForDaysQuestion: Int, typeExercisesIsMoreThanDaysOfWeek: (String, List<String>?, List<String>?) -> Boolean): Boolean {

    return if(selectedResponsesDb.toSet() != newSelectedResponses.toSet()) {
        if(newSelectedResponses.isNotEmpty()){
            when(changedQuestion) {
                "Are you more into calisthenics or gym workouts?" -> selectedResponsesDb[0] != "Both"
                "What are your goals?" -> newSelectedResponses.any { it == "Build more muscle" }
                "What types of calisthenics exercises do you focus on or want to focus on?" -> typeExercisesIsMoreThanDaysOfWeek(changedQuestion, null, newSelectedResponses)
                "What types of gym exercises do you focus on or want to focus on?" -> typeExercisesIsMoreThanDaysOfWeek(changedQuestion, newSelectedResponses, null)
                "Which days of the week can/do you want to train?" -> numberTypeExerciseForDaysQuestion > newSelectedResponses.size
                else -> false
            }
        } else false
    } else false

}

fun getActualNumberTypeExercises(newAnsweredQuestion: Map<String, List<String>>): Int {
    return (newAnsweredQuestion["What types of gym exercises do you focus on or want to focus on?"]?.size ?: 0) +
            (newAnsweredQuestion["What types of calisthenics exercises do you focus on or want to focus on?"]?.size ?: 0)
}

fun getQuestionForRemove(question: String, newSelectedResponses: List<String>, daysOfWeek: Int, typeExercises: Int, selectedResponsesDb: List<String>, allInitialQuestions: List<Question>): String? {
    var c: String? = null

    when (question) {
        "Are you more into calisthenics or gym workouts?" -> {
            if(selectedResponsesDb[0] == "Both")
                c = if(newSelectedResponses[0] == "Gym") "What types of calisthenics exercises do you focus on or want to focus on?"
                else "What types of gym exercises do you focus on or want to focus on?"
        }

        "What are your goals?" -> if("Build more muscle" !in newSelectedResponses && "Build more muscle" in selectedResponsesDb ) c = "What types of gym exercises do you focus on or want to focus on?" else c = null
        else -> c = null
    }

    return c
}