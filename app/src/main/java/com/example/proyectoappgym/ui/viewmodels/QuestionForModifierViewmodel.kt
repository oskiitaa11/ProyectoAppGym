package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuestionForModifierViewmodel(private val userDatabase: RepositoryUserDatabase, val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    var allQuestions = repositoryQuestions.allQuestions()

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
}