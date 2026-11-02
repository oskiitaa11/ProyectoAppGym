package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_auth.AuthRepository
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
class SharedRegistrationViewmodel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private var _userWithExistedEmail: MutableStateFlow<User?> = MutableStateFlow(null)
    val userWithExistedEmail = _userWithExistedEmail.asStateFlow()
    private var _emailExists: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val emailExist = _emailExists.asStateFlow()
    private var _errorToAddUser: MutableStateFlow<ErrorForShowing?> = MutableStateFlow(null)
    val errorToAddUser = _errorToAddUser.asStateFlow()
    private var _userCreatedDone: MutableSharedFlow<Boolean> = MutableStateFlow(false)
    val userCreatedDone = _userCreatedDone.asSharedFlow()

    fun addUserToCreate(user: User) {
        _userWithExistedEmail.update { user }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            try {
                _errorToAddUser.update { null }
                authRepository.signUp(user)
                _userCreatedDone.emit(true)
            } catch (e: SocketTimeoutException) {
                _errorToAddUser.update { ErrorForShowing("Error connecting to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN) }
            } catch (e: ErrorException) {
                if (e.error == "email_already_exists")
                    _emailExists.emit(true)
                else _errorToAddUser.update { getErrorForShowingScreen(e.error, e.message) }
            }
        }
    }

    fun setEmailExists() {
        _emailExists.update { true }
    }

    fun setExistsEmailToNull() {
        _emailExists.update { null }
    }

    fun setErrorToAddUserToNull() {
        _errorToAddUser.update { null }
    }
}