package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationQuestionsViewmodel(private val repositoryQuestions: RepositoryQuestions, private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var allQuestions: List<Question> = repositoryQuestions.allQuestions()
    var thereIsErrorToAddUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun addUser(user: User) {
        viewModelScope.launch {
           thereIsErrorToAddUser.update { !userDatabase.addUser(user) }
        }

        var o = 1
    }

    fun setThereIsErrorToNull() {
        thereIsErrorToAddUser.update { null }
    }

}