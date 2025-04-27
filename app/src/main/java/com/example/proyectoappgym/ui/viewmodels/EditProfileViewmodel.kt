package com.example.proyectoappgym.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewmodel(private val userDatabase: RepositoryUserDatabase, val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    var allQuestions = listOf<Question>()

    init {
        viewModelScope.launch {
            currentUser = userDatabase.getCurrentUser() as MutableStateFlow<User>
            allQuestions = repositoryQuestions.allQuestions()
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            userDatabase.updateNameCurrentUser(newName)
        }
    }

    fun updateAvatarProfile(newAvatar: Int) {
        viewModelScope.launch {
            userDatabase.updateAvatarProfile(newAvatar)
        }
    }
}