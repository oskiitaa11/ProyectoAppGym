package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration.allQuestions
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Avatars
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewmodel(private val userDatabase: RepositoryUserDatabase, val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    //var allQuestions = MutableStateFlow(emptyList<Question>())
    var allQuestions = repositoryQuestions.allQuestions()
    var showDialog = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            userDatabase.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            userDatabase.updateNameCurrentUser(newName)
        }
    }

    fun updateAvatarProfile(newAvatar: Avatars) {
        viewModelScope.launch {
            userDatabase.updateAvatarProfile(newAvatar)
        }
    }

    fun updateShowDialog() {
        showDialog.update { !showDialog.value }
    }

    /*fun removeQuestion(question: Question) {
        allQuestions.update {
            allQuestions.value.filter { it.id != question.id }
        }
    }

    fun addQuestion(question: Question) {
        var newAllQuestions = allQuestions.value.toMutableList().apply {
            add(question.id - 1, question)
        }.toList()

        allQuestions.update {
            newAllQuestions
        }
    }*/
}