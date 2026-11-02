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
class UpdatePasswordProfileViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _error: MutableStateFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asStateFlow()
    private var _validationDone: MutableSharedFlow<Boolean> = MutableStateFlow(false)
    val validationDone = _validationDone.asSharedFlow()
    private var _isInvalidCredential: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInvalidCredentials = _isInvalidCredential.asStateFlow()

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

    fun updatePassword(actualPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                userRepository.updatePassword(actualPassword, newPassword)
                _validationDone.emit(true)
            } catch (e: SocketTimeoutException) {
                _error.update { ErrorForShowing("Error connecting to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN) }
            } catch (e: ErrorException) {
                if (e.error == "current_password_invalid") _isInvalidCredential.update { true }
                else _error.update { getErrorForShowingScreen(e.error, e.message) }
            }
        }
    }

    fun setErrorToNull() {
        viewModelScope.launch {
            _error.update { null }
        }
    }

    fun setIsInvalidCredentialToFalse() {
        viewModelScope.launch {
            _isInvalidCredential.update { false }
        }
    }
}