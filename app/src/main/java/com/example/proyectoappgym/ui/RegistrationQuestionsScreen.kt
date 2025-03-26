package com.example.proyectoappgym.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.entity.User

@SuppressLint("UnrememberedMutableState")
@Composable
fun RegistrationQuestionsScreen(user: User, allQuestions: List<Question>) {
    var progress by remember { mutableIntStateOf(0) }
    var allChecked = remember { mutableStateMapOf<String, MutableList<Boolean>>() }
    var indexForCheckedList = remember { 0 }

    Column(modifier = Modifier.fillMaxSize().background(color = colorResource(R.color.lightBlack))) {
        LinearProgressIndicator(
            progress = { progress.toFloat() }
        )

        for(question in allQuestions) {
            ShowQuestion(question)
            allChecked[question.question] = mutableListOf()
            for (response in question.responses) {
                allChecked[question.question]?.plus(false)
                ShowRowCheckbox(response, { newChecked -> allChecked[question.question]?.set(indexForCheckedList, newChecked) })
                indexForCheckedList++
            }

            progress++
        }

    }

}

@Composable
fun ShowQuestion(question: Question){
    Text(question.question, color = Color.White)
    if(question.responsesTypes == ResponsesType.CHECKBOX) {
        for (response in question.responses) {
            ShowRowCheckbox(response, {  })
        }
    }
}

@Composable
fun ShowRowCheckbox(response: String, changeChecked: (Boolean) -> Unit){
    var isChecked by remember { mutableStateOf(false) }

    Row {
        Checkbox(
            checked = isChecked,
            onCheckedChange = changeChecked
        )
        Text(response)
    }
}