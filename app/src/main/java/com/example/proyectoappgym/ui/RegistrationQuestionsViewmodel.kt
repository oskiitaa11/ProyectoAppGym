package com.example.proyectoappgym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_questions.RepositoryQuestions
import com.example.proyectoappgym.entity.Question
import kotlinx.coroutines.launch

class RegistrationQuestionsViewmodel(val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var allQuestions: List<Question> = listOf()

    init {
        viewModelScope.launch {
            allQuestions = repositoryQuestions.allQuestions()
        }
    }
}