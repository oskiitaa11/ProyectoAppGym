package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _error: MutableSharedFlow<String?> = MutableStateFlow(null)
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            val user: User

            try {
                user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: ErrorException) {
                _error.emit(e.message)
            }
        }
    }

}