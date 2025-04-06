package com.example.proyectoappgym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.launch

class RegistrationQuestionsViewmodel(private val repositoryQuestions: RepositoryQuestions, private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var allQuestions: List<Question> = listOf()

    init {
        viewModelScope.launch {
            allQuestions = repositoryQuestions.allQuestions()
        }
    }

    fun addUser(user: User): Boolean {
        var isTaskCompleted = false

        viewModelScope.launch {
            isTaskCompleted = userDatabase.addUser(user)
        }

        return isTaskCompleted
    }

    fun userExist(username: String): Boolean {
        var userExist = false

        viewModelScope.launch {
            userExist = userDatabase.userExist(username)
        }

        return userExist
    }

}