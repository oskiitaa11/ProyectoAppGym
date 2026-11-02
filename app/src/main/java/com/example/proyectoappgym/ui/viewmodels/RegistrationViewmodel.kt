package com.example.proyectoappgym.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.C
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.data.request.RequestExistingUsername
import com.example.proyectoappgym.entity.data.request.ResponseExistingUser
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.ui.screens.validateTextUsername
import com.google.gson.internal.NonNullElementWrapperList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class RegistrationViewmodel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private var _email: MutableStateFlow<String> = MutableStateFlow("")
    val email = _email.asStateFlow()
    private var _serverNotConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val serverNotConnected = _serverNotConnected.asStateFlow()
    private var idFromExistsUser: Long? = null
    private var _errorWithUsername: MutableStateFlow<String> = MutableStateFlow("")
    val errorWithUsername = _errorWithUsername.asStateFlow()
    private var _fieldUsername: MutableStateFlow<String> = MutableStateFlow("")
    val fieldUsername = _fieldUsername.asStateFlow()
    private var _isFocusable: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFocusable = _isFocusable.asStateFlow()

    init {
        observeUsername()
    }

    fun setServerNotConnectedToFalse() {
        _serverNotConnected.update { false }
    }

    private fun observeUsername() {
        viewModelScope.launch {


            try {
                _fieldUsername.debounce(1000).distinctUntilChanged().collect {
                    if (_fieldUsername.value.isEmpty()){
                        _errorWithUsername.update { "" }
                        return@collect
                    }

                    if (_isFocusable.value)
                        verifyIfExistsUsername(it)
                }

            } catch (e: SocketTimeoutException) {
                _serverNotConnected.update { true }
            }
        }
    }

    fun updateFieldUsername(newText: String) {
        _fieldUsername.update { newText }
    }

    fun updateEmail(newEmail: String) {
        if (newEmail.isNotEmpty() && newEmail != _email.value)
            _email.update { newEmail }
    }

    fun updateIsFocusable(newIsFocusable: Boolean) {
        _isFocusable.update { newIsFocusable }
    }

    private suspend fun verifyIfExistsUsername(newUsername: String) {
        var response: Response<ResponseExistingUser>? = null
        val isError = validateTextUsername(
            newUsername.trim(),
            newUsername.trim() == _email.value.split(Regex("@"))[0],
            { errorText -> _errorWithUsername.update { errorText } }
        )

        if (newUsername.isNotEmpty() && !isError){
            response = authRepository.existsUser(newUsername, idFromExistsUser)
        }

        if (response?.isSuccessful == true && response.body() != null) {
            if (response.body()?.existsUser == true) _errorWithUsername.update { "User already exists" }
            else _errorWithUsername.update { "" }
            idFromExistsUser = response.body()?.idForClient
        }

        if (!isError && response == null) _errorWithUsername.update { "" }

    }

    fun callToVerifyUsername() {
        viewModelScope.launch {
            try {
                verifyIfExistsUsername(_fieldUsername.value)
            } catch (e: SocketTimeoutException) {
                _serverNotConnected.update { true }
            }
        }
    }

}