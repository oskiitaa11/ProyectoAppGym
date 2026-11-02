package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class UpdateEmailProfileViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _error: MutableStateFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asStateFlow()
    private var _newValueExists: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val newValueExists = _newValueExists.asStateFlow()
    private var _validationDone: MutableSharedFlow<Boolean> = MutableStateFlow(false)
    val validationDone = _validationDone.asSharedFlow()

    init {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.update { ErrorForShowing("Error connecting to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN) }
            } catch (e: ErrorException) {
                _error.update { getErrorForShowingScreen(e.error, e.message) }
            }
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            try {
                userRepository.updateEmail(newEmail)
                _validationDone.emit(true)
            } catch (e: SocketTimeoutException) {
                _error.update { ErrorForShowing("Error connecting to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN) }
            } catch (e: ErrorException) {
                if (e.error == "email_already_exists")
                    _newValueExists.update { true }
                else _error.update { getErrorForShowingScreen(e.error, e.message) }
            }
        }
    }

    fun setErrorToNull() {
        viewModelScope.launch {
            _error.update { null }
        }
    }

    fun setEmailExistsToNull() {
        viewModelScope.launch {
            _newValueExists.update { null }
        }
    }
}