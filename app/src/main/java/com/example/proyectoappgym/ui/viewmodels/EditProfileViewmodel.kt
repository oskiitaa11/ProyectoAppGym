package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.UserApiService
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.data.DataUserRequest
import com.example.proyectoappgym.entity.users.Avatars
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewmodel @Inject constructor(private val userRepository: UserRepository, val repositoryQuestions: RepositoryQuestions): ViewModel() {
    var currentUser = MutableStateFlow(User())
    //var allQuestions = MutableStateFlow(emptyList<Question>())
    var allQuestions = repositoryQuestions.allQuestions()
    var showDialog = MutableStateFlow(true)

    init {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }
    
    fun updateCurrentUser(dataUserRequest: DataUserRequest) {
        viewModelScope.launch {
            userRepository.updateCurrentUser(dataUserRequest).collect { user -> currentUser.value = user }
        }
    }
    /*fun updateName(newName: String) {
        viewModelScope.launch {
            userApiService.updateNameCurrentUser(newName)
        }
    }

    fun updateAvatarProfile(newAvatar: Avatars) {
        viewModelScope.launch {
            userApiService.updateAvatarProfile(newAvatar)
        }
    }*/

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