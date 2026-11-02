package com.example.proyectoappgym.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.entity.data.request.SignInUser
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.entity.functions.getErrorForShowingScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private var _errorToSignIn: MutableStateFlow<ErrorForShowing?> = MutableStateFlow(null)
    val errorToSignIn = _errorToSignIn.asStateFlow()

    fun signIn(password: String, email: String) {
        viewModelScope.launch {
            val authUser = SignInUser(password, email)

            try {
                authRepository
                authRepository.signIn(authUser)
            } catch (e: SocketTimeoutException) {
                _errorToSignIn.update { ErrorForShowing("Error connecting to the server", R.drawable.ic_not_connecting, ErrorType.ERROR_IN_ERROR_SCREEN) }
            } catch (e: ErrorException) {
                _errorToSignIn.update { getErrorForShowingScreen(e.error, e.message) }
            }
        }
    }

    fun updateErrorToNullToSignIn() {
        viewModelScope.launch {
            _errorToSignIn.emit(null)
        }
    }
}