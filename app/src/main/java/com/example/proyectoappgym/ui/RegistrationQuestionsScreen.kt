package com.example.proyectoappgym.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.entity.User
import java.nio.file.WatchEvent
import kotlin.inc

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun RegistrationQuestionsScreen(user: User, allQuestions: List<Question>) {
    //Asigno una lista de la clase Pairs(lista de clave-valor) para introducirla despues en el metodo mutableStateMapOf()
    var progress by remember { mutableIntStateOf(6) }
    val allChecked = remember {
        mutableMapOf<String, SnapshotStateMap<String, Boolean>>().apply {
            putAll(getInitialQuestionsMap(allQuestions))
        }
    }
    lateinit var actualQuestion: Question
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.lightBlack))
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
            targetState = progress,
            animationSpec = tween(durationMillis = 800)
        ) { targetState ->
            actualQuestion = allQuestions[targetState]

            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 30.dp).padding(top = 45.dp)
            ) {

                /*Si ha llegado a la utlima pregunta que muestra una funcion distinta,
               para poder mostrar todos las respuestas de esta*/
                if(actualQuestion != allQuestions.last()) {
                    ShowQuestion(actualQuestion, allChecked.getValue(actualQuestion.question))
                } else {
                    ShowLastQuestion(actualQuestion, allChecked.getValue(actualQuestion.question))
                }

                if(showError) ShowErrorText("You must answer to the questions", 0.dp)
            }

        }

    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ShowButtonForNextOrPreviousQuestion("Back", R.drawable.ic_arrow_back_ios_new_24,
                {
                    if(showError) showError = false
                    if(progress>0) progress--
                }
            )
            ShowButtonForNextOrPreviousQuestion(
                "Next", R.drawable.ic_arrow_forward_ios_24,
                {
                    //Si no hay ninguna respuesta a true se asigna true a showError
                    if(actualQuestion != allQuestions.last()) showError = allChecked[actualQuestion.question]?.all { !it.value } as Boolean
                    if(!showError) progress++
                }
            )
        }

    }

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
    var responsesSecondColumn = question.responses.slice(4..question.responses.lastIndex)

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
            responsesFirstColumn.forEach { ShowRowCheckbox(it, allCheckedActualQuestions[it] as Boolean, { allCheckedActualQuestions[it] = !allCheckedActualQuestions[it]!! }, question.responsesTypes) }
        }

        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            responsesSecondColumn.forEach { ShowRowCheckbox(it, allCheckedActualQuestions[it] as Boolean, { allCheckedActualQuestions[it] = !allCheckedActualQuestions[it]!! }, question.responsesTypes) }
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

