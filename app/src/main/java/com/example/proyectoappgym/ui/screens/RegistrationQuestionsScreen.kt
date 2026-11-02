package com.example.proyectoappgym.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.proyectoappgym.LoginRoute
import com.example.proyectoappgym.R
import com.example.proyectoappgym.RegistrationQuestionsRoute
import com.example.proyectoappgym.RegistrationRoute
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration.allQuestions
import com.example.proyectoappgym.entity.questions.BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.BUILD_MUSCLE_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.GYM_QUESTION
import com.example.proyectoappgym.entity.questions.GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION
import com.example.proyectoappgym.entity.questions.HOW_MANY_DAYS_FOR_TRAINING
import com.example.proyectoappgym.entity.questions.PLACE_WHERE_WORKOUT_GYM_QUESTION
import com.example.proyectoappgym.entity.trainingroutines.DayOfWeek
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.ResponsesType
import com.example.proyectoappgym.entity.questions.STRENGTH_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.TENS_RESPONSE_FOR_GOALS_QUESTION
import com.example.proyectoappgym.entity.questions.WHERE_TRAIN_CALISTHENICS
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.graphs.RegistrationGraphRoute
import com.example.proyectoappgym.ui.viewmodels.RegistrationQuestionsViewmodel
import com.example.proyectoappgym.ui.viewmodels.RegistrationViewmodel
import com.example.proyectoappgym.ui.viewmodels.SharedRegistrationViewmodel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.text.ifEmpty

fun NavGraphBuilder.registrationQuestionsDestination(navController: NavController) {
    composable<RegistrationQuestionsRoute> { navBackStackEntry ->
        val parentEntry = remember(navBackStackEntry) {
            navController.getBackStackEntry(RegistrationGraphRoute)
        }
        val registrationQuestionsRoute: RegistrationQuestionsRoute = navBackStackEntry.toRoute()
        val sharedRegistrationViewmodel: SharedRegistrationViewmodel = hiltViewModel(parentEntry)
        val registrationQuestionsViewmodel: RegistrationQuestionsViewmodel = hiltViewModel()
        val allQuestions = registrationQuestionsViewmodel.allQuestions
        val context = LocalContext.current
        var user : User
        with(registrationQuestionsRoute) {
            user = User(username, password, email, name.ifEmpty { username }, birthdate, gender)
        }

        LaunchedEffect(registrationQuestionsViewmodel.userWithExistedEmail) {
            registrationQuestionsViewmodel.userWithExistedEmail.distinctUntilChanged().collect {
                if (it != null) {
                    sharedRegistrationViewmodel.addUserToCreate(it)
                    sharedRegistrationViewmodel.setEmailExists()
                    navController.popBackStack()
                }
            }
        }

        LaunchedEffect(registrationQuestionsViewmodel.errorToAddUser) {
            registrationQuestionsViewmodel.errorToAddUser.distinctUntilChanged().collect {
                if (it != null)
                    if (it.isEmpty()){
                        Toast.makeText(context, "User created", Toast.LENGTH_LONG).show()
                        navController.popBackStack(LoginRoute, false)
                    } else {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        navController.popBackStack()
                    }
            }
        }

        RegistrationQuestionsScreen(
            user,
            allQuestions,
            { userForAdd -> registrationQuestionsViewmodel.addUser(userForAdd) }
        )
    }
}

@SuppressLint("UnrememberedMutableState", "RememberReturnType")
@Composable
fun RegistrationQuestionsScreen(
    user: User,
    allQuestions: List<Question>,
    addUser: (User) -> Unit,
) {
    //Asigno una lista de la clase Pairs(lista de clave-valor) para introducirla despues en el metodo mutableStateMapOf()
    var progress by rememberSaveable { mutableIntStateOf(0) }
    val allChecked = rememberSaveable {
        mutableMapOf<String, SnapshotStateMap<String, Boolean>>().apply {
            putAll(getInitialQuestionsMap(allQuestions))
        }
    }
    val allQuestionsScreen = rememberSaveable {
        allQuestions.toMutableStateList()
    }
    lateinit var actualQuestion: Question
    var showError by rememberSaveable { mutableStateOf(false) }
    val necessariesDaysForTraining = rememberSaveable { 0 }
    var showWaitingDialog by rememberSaveable { mutableStateOf(false) }
    var textForShowInDialog by rememberSaveable { mutableStateOf("") }
    var isResponseBuildMoreMuscleWithGym by rememberSaveable { mutableStateOf(false) }
    val state = rememberScrollState()

    if(showWaitingDialog) {
        ShowWaitingDialog(textForShowInDialog)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(state)
            .background(color = colorResource(R.color.lightBlack))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.2f)) {
                Image(
                    painter = painterResource(R.drawable.background_image4),
                    contentDescription = "Imagen de fondo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = "We need to know more about you",
                fontSize = 40.sp,
                color = colorResource(R.color.lightGreen),
                lineHeight = 40.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 30.dp)
            )
            LinearProgressIndicator(
                /*Divido entre 10, ya que el LinearProgress tiene un rango entre
                0.0 y 1.0 y multiplico por 1,55 porque al ser de 0.0 a 1.0, hay 10 pasos
                para que el indicador llegue al final y solo son 7 preguntas, entonces mutiplico por 1,55
                para que el valor vaya aunmentando mas en funcion de progress*/
                progress = { (progress.toFloat() * 1.55f) / 10 },
                modifier = Modifier.padding(start = 30.dp, top = 70.dp, end = 30.dp).fillMaxWidth()
            )

            Crossfade(
                targetState = if(progress > allQuestionsScreen.size - 1) progress - 1 else progress,
                animationSpec = tween(durationMillis = 800),
                label = "Questions"
            ) { targetState ->
                actualQuestion = allQuestionsScreen[targetState]

                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 30.dp).padding(top = 45.dp)
                ) {
                    if (actualQuestion != allQuestionsScreen.last()) {
                        ShowQuestion(actualQuestion, allChecked.getValue(actualQuestion.question))
                    } else {
                        ShowLastQuestion(actualQuestion, allChecked.getValue(actualQuestion.question))
                    }

                    if(showError && actualQuestion == allQuestionsScreen.last()) {
                        ShowErrorText("You choose $necessariesDaysForTraining answers to the questions", 0.dp)
                    } else if (showError) ShowErrorText("You choose a answer to the questions", 0.dp)
                }

            }

        }

        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 50.dp).fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                ShowButtonForNextOrPreviousQuestion("Back", R.drawable.ic_arrow_back_ios_new_24) {
                    if (showError) showError = false
                    if (progress > 0) progress--
                    isResponseBuildMoreMuscleWithGym = false
                }
                ShowButtonForNextOrPreviousQuestion(
                    "Next", R.drawable.ic_arrow_forward_ios_24
                ) {
                    showError = allChecked[actualQuestion.question]?.all { !it.value } as Boolean
                    /*Si no hay ninguna respuesta a true se asigna true a showError,
                    siempre que sea la ultima pregunta*/
                    if (actualQuestion == allQuestionsScreen.last() && !showError) {
                        addUser(user, allChecked,
                            { newTextForShowInDialog ->
                                textForShowInDialog = newTextForShowInDialog
                                showWaitingDialog = true },
                            addUser)
                    } else if(actualQuestion.id == 0) {
                        if(allChecked[CALISTHENICS_OR_GYM_QUESTION]?.get(
                                CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) == true ||
                            allChecked[CALISTHENICS_OR_GYM_QUESTION]?.get(BOTH_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) == true){
                            allQuestionsScreen.find { it.question == GOALS_QUESTION }?.addResponse(TENS_RESPONSE_FOR_GOALS_QUESTION)
                            allChecked[GOALS_QUESTION]?.put(TENS_RESPONSE_FOR_GOALS_QUESTION, false)
                        }
                        else {
                            allQuestionsScreen.find { it.question == GOALS_QUESTION }?.removeResponse(TENS_RESPONSE_FOR_GOALS_QUESTION)
                            allChecked[GOALS_QUESTION]?.remove(TENS_RESPONSE_FOR_GOALS_QUESTION)
                        }

                    } else if (actualQuestion.question == GOALS_QUESTION) { //Si la respuesta respondida es la primera
                        chooseQuestionAccordingToAnswerByFirstQuestion(
                            allChecked[CALISTHENICS_OR_GYM_QUESTION]?.getValue(CALISTHENICS_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) == true,
                            allChecked[CALISTHENICS_OR_GYM_QUESTION]?.getValue(
                                GYM_RESPONSE_FOR_CALISTHENICS_OR_GYM_QUESTION) == true,
                            allQuestionsScreen
                        )

                        disableResponseAccordingToResponse(allChecked[GOALS_QUESTION]?.filter { (_, isSelected) -> isSelected }?.keys?.toList()
                            ?: emptyList()) { isResponseBuildMoreMuscleWithGym = true }
                    }

                    if (!showError) progress++
                    //Cuando la pregunta sea la primera, según la respuesta elegida le aparecerá una pregunta u otro

                }

            }

        }
    }

}

@Composable
fun ShowQuestion(question: Question, allCheckedActualQuestions: MutableMap<String, Boolean>){
    Text(
        question.question,
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 25.dp)
    )

    for (response in question.responses) {
        ShowRowCheckbox(
            response,
            allCheckedActualQuestions[response] as Boolean,
            {
                /*Por cada respuesta se asigna el valor a false por si existe alguna respuesta
                a true, ya que con radioButton solo se puede poner una respuesta a true.
                Si es un checkbox se asigna el valor que ya tiene, dejandose igual*/
                if(question.responsesTypes == ResponsesType.RADIOBUTTON)
                    allCheckedActualQuestions.forEach { (responseList, _) ->
                        allCheckedActualQuestions[responseList] = false
                    }
                allCheckedActualQuestions[response] = !allCheckedActualQuestions[response]!!
            },
            question.responsesTypes
        )
    }
}

@Composable
fun ShowLastQuestion(question: Question, allCheckedActualQuestions: MutableMap<String, Boolean>){
    val responsesFirstColumn = question.responses.slice(0..3)
    val responsesSecondColumn = question.responses.slice(4..question.responses.lastIndex)

    Text(
        question.question,
        color = Color.White,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(bottom = 25.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.5f),
            verticalArrangement = Arrangement.Top
        ) {
            responsesFirstColumn.forEach {
                ShowRowCheckbox(it,
                    allCheckedActualQuestions[it] as Boolean,
                    { allCheckedActualQuestions[it] = !allCheckedActualQuestions[it]!! },
                    question.responsesTypes)
            }
        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            responsesSecondColumn.forEach {
                ShowRowCheckbox(it,
                    allCheckedActualQuestions[it] as Boolean,
                    { allCheckedActualQuestions[it] = !allCheckedActualQuestions[it]!! },
                    question.responsesTypes)
            }
        }
    }

}

@Composable
fun ShowRowCheckbox(response: String, isChecked: Boolean, changeChecked: () -> Unit, responseType: ResponsesType){
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 20.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(responseType == ResponsesType.CHECKBOX) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { changeChecked() },
                colors = CheckboxColors(Color.White, Color.Transparent, colorResource(R.color.lightGreen), Color.Transparent, Color.LightGray, Color.LightGray, Color.LightGray, Color.White, Color.White, Color.LightGray, Color.LightGray, Color.LightGray),
                modifier = Modifier.size(40.dp)
            )

        } else {
            RadioButton(
                selected = isChecked,
                onClick = changeChecked,
                colors = RadioButtonColors(colorResource(R.color.lightGreen), Color.White, Color.LightGray, Color.LightGray),
                modifier = Modifier.size(40.dp)
            )
        }
        Text(response, color = Color.White)
    }
}

@Composable
fun ShowButtonForNextOrPreviousQuestion(label: String, idIcon: Int, onClick: () -> Unit) {
    TextButton(
        shape = ShapeDefaults.ExtraSmall,
        border = BorderStroke(2.dp, Color.White),
        onClick = onClick,
        contentPadding = PaddingValues(20.dp, 10.dp),
    ) {
        if(label.lowercase() == "back") {
            Icon(
                painter = painterResource(idIcon),
                contentDescription = label,
                tint = Color.White
            )
        }

        Text(label, color = Color.White)

        if(label.lowercase() == "next") {
            Icon(
                painter = painterResource(idIcon),
                contentDescription = label,
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowWaitingDialog(text: String) {
    BasicAlertDialog(
        onDismissRequest = {  },
        modifier = Modifier.fillMaxWidth().background(Color.White, ShapeDefaults.Medium),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text, color = colorResource(R.color.lightBlack), fontSize = 15.sp, modifier = Modifier.padding(horizontal = 10.dp))
            CircularProgressIndicator(
                trackColor = Color.White,
                color = colorResource(R.color.lightBlack),
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

fun addUser(user: User, allChecked: Map<String, MutableMap<String, Boolean>>, setShowWaitingDialog: (String) -> Unit, addUser: (User) -> Unit) {
    user.changeAllAnsweredQuestions(getAllQuestionAnswered(allChecked))
    addUser(user)
    setShowWaitingDialog("Adding user")
}

fun getInitialQuestionsMap(allQuestions: List<Question>): List<Pair<String, SnapshotStateMap<String, Boolean>>> {
    val allPairs: MutableList<Pair<String, SnapshotStateMap<String, Boolean>>> = mutableListOf()

    /* Cada pregunta va con un map de String-Boolean para representar a los checkBox
    de cada respuesta */
    for(question in allQuestions) {
        allPairs.add(//Añado la lista de Pairs
            Pair(//Para cada Pair añado el string de la pregunta y las respuestas
                question.question,
                mutableStateMapOf<String, Boolean>().apply {//Le inserto los valores por defecto(false) de cada respuesta
                    putAll(//Con este metodo puedo insertar una lista de Pairs
                        question.responses.map {
                                response -> Pair(response, false)
                        }
                    )
                }
            )
        )
    }

    return allPairs.toList()
}

fun chooseQuestionAccordingToAnswerByFirstQuestion(isCalisthenicsResponse: Boolean, isGymResponse: Boolean, allQuestionsScreen: MutableList<Question>) {
    val gymQuestion: Question
    val questionForDuplicate: Question
    val calisthenicsQuestion: Question

    if(isCalisthenicsResponse) {
        addAndRemoveQuestions(WHERE_TRAIN_CALISTHENICS, allQuestionsScreen,
            PLACE_WHERE_WORKOUT_GYM_QUESTION, 2)
    } else if(isGymResponse) {
        addAndRemoveQuestions(PLACE_WHERE_WORKOUT_GYM_QUESTION, allQuestionsScreen, WHERE_TRAIN_CALISTHENICS, 3)
        allQuestionsScreen.find { it.question == GOALS_QUESTION }?.removeResponse(TENS_RESPONSE_FOR_GOALS_QUESTION)
    } else { // Si la respuesta es la tercera se añade las dos preguntas si no estan en la lista de preguntas mutable
        questionForDuplicate = allQuestions.first { it.question == PLACE_WHERE_WORKOUT_GYM_QUESTION }
        gymQuestion = Question(questionForDuplicate.id, questionForDuplicate.question, questionForDuplicate.responsesTypes, *questionForDuplicate.responses)
        calisthenicsQuestion = allQuestionsScreen.first { it.question == WHERE_TRAIN_CALISTHENICS }.copy()

        if(!isQuestionInAllQuestions(calisthenicsQuestion, allQuestionsScreen))
            allQuestionsScreen.add(3, calisthenicsQuestion)
        if(!isQuestionInAllQuestions(gymQuestion, allQuestionsScreen)) allQuestionsScreen[2] = gymQuestion
    }
}

fun disableResponseAccordingToResponse(checkedResponses: List<String>, disableResponse: () -> Unit) {
    if(checkedResponses.contains(STRENGTH_RESPONSE_FOR_GOALS_QUESTION)) disableResponse()
}

fun addAndRemoveQuestions(questionString: String, allQuestionsScreen: MutableList<Question>, questionStringForRemove: String, indexForInsertQuestion: Int) {
    val questionForAdd = allQuestions.find { it.question == questionString } ?: Question(0, "", ResponsesType.RADIOBUTTON)
    val newQuestionForAddQuestion = Question(questionForAdd.id, questionForAdd.question, questionForAdd.responsesTypes, *questionForAdd.responses)

    /* Si la respuesta elegida es la primera, se coge, se comprueba si la pregunta está en la lista mutable de preguntas.
       Si no está se añade y se borra la pregunta, segun la otra respuesta elegida  */
    if(!isQuestionInAllQuestions(newQuestionForAddQuestion, allQuestionsScreen)) allQuestionsScreen.add(indexForInsertQuestion, newQuestionForAddQuestion)
    allQuestionsScreen.removeIf { it.question == questionStringForRemove }
    joinListsWithSameQuestion(allQuestionsScreen, newQuestionForAddQuestion.responses.toList())
}

fun isQuestionInAllQuestions(question: Question, allQuestionsScreen: List<Question>): Boolean{
    return allQuestionsScreen.any { questionInLoop -> questionInLoop == allQuestions.find { it == question } }
}

fun getAllQuestionAnswered(allChecked: Map<String, MutableMap<String, Boolean>>): MutableMap<String, List<String>> {
    val allQuestionsAnswered = mutableMapOf<String, List<String>>()
    val sortedDaysOfWeek = allChecked[HOW_MANY_DAYS_FOR_TRAINING]?.filter{ (_, isSelected) -> isSelected }?.keys?.sortedBy { dayOfWeek-> DayOfWeek.fromString(dayOfWeek).idDay } ?: emptyList()
    val allCheckedMutable = allChecked.toMutableMap()
    val pairs = allCheckedMutable.map { (question, map) -> Pair(question, map.filter { (_, value) -> value }.keys.toList()) }.toMutableList()

    allCheckedMutable.remove(HOW_MANY_DAYS_FOR_TRAINING)
    pairs.add(Pair(HOW_MANY_DAYS_FOR_TRAINING, sortedDaysOfWeek))
    allQuestionsAnswered.putAll(pairs)

    return allQuestionsAnswered
}

private fun joinListsWithSameQuestion(allQuestionsScreen: MutableList<Question>, addedResponses: List<String>) {
    val newAllQuestionsScreen = allQuestionsScreen.groupBy { it.question }.map { (_, questions) ->
        if(questions.size >= 2) {
            if(questions[0].responses.toSet() == addedResponses.toSet()) Question(questions[0].id, questions[0].question, questions[0].responsesTypes, *questions[0].responses)
            else Question(questions[1].id, questions[1].question, questions[1].responsesTypes, *questions[1].responses)
        } else Question(questions[0].id, questions[0].question, questions[0].responsesTypes, *questions[0].responses)
    }.toMutableList()

    allQuestionsScreen.clear()
    allQuestionsScreen.addAll(newAllQuestionsScreen)
}