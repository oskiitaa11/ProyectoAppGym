package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.data.DataUserRequest
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionForModifierViewmodel @Inject constructor(private val userRepository: UserRepository, private val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    var allQuestions = repositoryQuestions.allQuestions()
    var isChangeRoutine: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            currentUser = userRepository.getCurrentUser() as MutableStateFlow<User>
        }
    }

    /*fun updateResponsesOfQuestion(question: String, newResponses: List<String>) {
        viewModelScope.launch {
            userRepository.updateResponses(question, newResponses)
        }
    }

    fun removeResponsesOfQuestion(question: String) {
        viewModelScope.launch {
            userRepository.removeResponsesOfQuestion(question)
        }
    }*/

    fun changeWeeklyRoutine(
        newAnsweredQuestion: Map<String, List<String>>,
    ) {
        val currentUserToUpdate = currentUser.value
        val dataUserRequest = DataUserRequest(currentUserToUpdate.username, currentUserToUpdate.name,
            currentUserToUpdate.birthdate, currentUserToUpdate.gender, newAnsweredQuestion,
            currentUserToUpdate.idProfileAvatar)
        var newUser = User()

        viewModelScope.launch {
            userRepository.updateCurrentUser(dataUserRequest).collect { newUser = it }
            currentUser.update { newUser }
        }
    }
}