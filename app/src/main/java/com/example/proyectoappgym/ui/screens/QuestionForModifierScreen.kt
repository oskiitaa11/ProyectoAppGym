package com.example.proyectoappgym.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableOpenTarget
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.proyectoappgym.ui.viewmodels.EditProfileViewmodel
import com.example.proyectoappgym.ui.viewmodels.QuestionForModifierViewmodel
import com.google.firebase.database.collection.LLRBNode
import kotlinx.serialization.Serializable

@Serializable
data class QuestionForModifierRoute(val question: Question, val selectedResponses: List<String>)

fun NavController.goToQuestionForModifier(question: Question, selectedResponses: List<String>) {
    navigate(QuestionForModifierRoute(question, selectedResponses))
}

fun NavGraphBuilder.questionForModifierDestination(navController: NavController) {
    composable<QuestionForModifierRoute> { navBackStackEntry ->
        val questionForModifierViewmodel: QuestionForModifierViewmodel =
            viewModel(navBackStackEntry) {
                QuestionForModifierViewmodel(
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).userDatabase,
                    (get(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY) as App).repositoryQuestions
                )
            }
        val questionForModifier: Question = navBackStackEntry.toRoute()
        val selectedResponses: List<String> = navBackStackEntry.toRoute()
        val allResponsesDb =
            questionForModifierViewmodel.allQuestions[questionForModifier.id - 1].responses.toList()

        QuestionForModifierScreen(questionForModifier, allResponsesDb, selectedResponses)
    }
}

@Composable
fun QuestionForModifierScreen(
    questionForModifier: Question,
    allResponsesDb: List<String>,
    selectedResponses: List<String>,
    changeCorrectedResponse: (String, String) -> Unit
) {
    var selectedResponses = remember { mutableStateListOf(*selectedResponses.toTypedArray()) }
    var isMultipleResponse = questionForModifier.responsesTypes == ResponsesType.CHECKBOX

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.lightBlack)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(questionForModifier.question, fontSize = 20.sp, color = Color.White)
        Spacer(modifier = Modifier.height(20.dp))
        allResponsesDb.forEach { response ->
            ShowResponse(
                response,
                selectedResponses.any { it == response },
                if (isMultipleResponse) { newResponse, isSelected ->
                    if (isSelected) selectedResponses.add(newResponse) else selectedResponses.remove(newResponse)
                } else { newResponse, isSelected ->
                    if (isSelected) selectedResponses.add(newResponse) else selectedResponses.remove(newResponse)
                }
            )
        }

        if (selectedResponses.toSet() != allResponsesDb.toSet())
            ShowButtonsApplyAndCancel(
                changeCorrectedResponse(questionForModifier.question),
                {
                    answeredResponses.clear()
                    answeredResponses.addAll(answeredResponsesDb)
                }
            )

    }
}

