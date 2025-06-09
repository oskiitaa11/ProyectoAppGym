package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionForModifierViewmodel(private val userDatabase: RepositoryUserDatabase, val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    var allQuestions = repositoryQuestions.allQuestions()
    var isChangeRoutine: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            currentUser = userDatabase.getCurrentUser() as MutableStateFlow<User>
        }
    }

    fun updateResponsesOfQuestion(question: String, newResponses: List<String>) {
        viewModelScope.launch {
            userDatabase.updateResponses(question, newResponses)
        }
    }

    fun removeResponsesOfQuestion(question: String) {
        viewModelScope.launch {
            userDatabase.removeResponsesOfQuestion(question)
        }
    }

    fun changeWeeklyRoutine(
        newAnsweredQuestion: Map<String, List<String>>,
    ) {
        viewModelScope.launch {
            isChangeRoutine.update {
                userDatabase.changeWeeklyRoutine(
                    newAnsweredQuestion,
                )
            }
        }
    }
}