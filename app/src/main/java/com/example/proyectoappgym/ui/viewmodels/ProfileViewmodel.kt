package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import com.example.proyectoappgym.entity.trainingroutines.TrainingRoutine
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.remote.SessionManager
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
class ProfileViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private var _currentUser = MutableStateFlow(User())
    val currentUser = _currentUser.asStateFlow()
    private var _error: MutableSharedFlow<ErrorForShowing?> = MutableStateFlow(null)
    val error = _error.asSharedFlow()

    init {
        viewModelScope.launch {
            val user: User

            try {
                user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.error))
            }
        }
    }

    fun reloadUser() {
        var user: User

        viewModelScope.launch {
            try {
                user = userRepository.getCurrentUser()
                _currentUser.update { user }
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }

        }
    }

    fun deleteCurrentUser() {
        viewModelScope.launch {
            try {
                userRepository.deleteCurrentUser()
            } catch (e: SocketTimeoutException) {
                _error.emit(ErrorForShowing("Unable to connect to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN))
            } catch (e: ErrorException) {
                _error.emit(getErrorForShowingScreen(e.error, e.message))
            }
        }
    }
}