package com.example.proyectoappgym.ui

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType
import com.example.proyectoappgym.entity.User
import com.google.android.gms.common.api.Response

@Composable
fun RegistrationQuestionsScreen(user: User, allQuestions: List<Question>) {
    var progress by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize().background(color = colorResource(R.color.lightBlack))) {
        LinearProgressIndicator(
            progress = { progress.toFloat() }
        )

        for(question in allQuestions) {

        }

    }

}

@Composable
fun ShowQuestion(question: Question){
    Text(question.question, color = Color.White)
    if(question.responsesTypes == ResponsesType.CHECKBOX) {
        for (response in question.responses) {
            ShowRowCheckbox(response)
        }
    }
}

@Composable
fun ShowRowCheckbox(response: String){
    var isChecked by remember { mutableStateOf(false) }

    Row {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { newChecked -> isChecked = newChecked }
        )
        Text(response)
    }
}