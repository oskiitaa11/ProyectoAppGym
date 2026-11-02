package com.example.proyectoappgym.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.R
import com.example.proyectoappgym.entity.data.request.TokenResponse
import com.example.proyectoappgym.entity.exceptions.ErrorForShowing
import com.example.proyectoappgym.entity.exceptions.ErrorType
import com.example.proyectoappgym.remote.SessionManager
import com.example.proyectoappgym.ui.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SessionViewmodel @Inject constructor(private val sessionManager: SessionManager): ViewModel() {
    private var _sessionState = MutableStateFlow<SessionState>(SessionState.Loading)
    val sessionState = _sessionState.asStateFlow()
    private var _error: MutableSharedFlow<ErrorForShowing?> = MutableStateFlow(null)
    var error = _error.asSharedFlow()
    private var previousSessionState: MutableStateFlow<SessionState> = MutableStateFlow(SessionState.Loading)
    private var _showAnimation: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showAnimation = _showAnimation.asStateFlow()

    init {
        viewModelScope.launch {
            sessionManager.tokenResponse.collect {
                runBlocking { checkSession(it) }
            }
        }

    }
    fun checkSession(token: TokenResponse?) {
        viewModelScope.launch {
            try {

                val expiredAt = sessionManager.getExpiredAt()
                sessionManager.verifyToken(token, expiredAt)
                previousSessionState.value = sessionState.value
                _sessionState.update { if (token != null) SessionState.Authenticated else SessionState.Unauthenticated }
                if (previousSessionState.value != SessionState.Loading) _showAnimation.value = true
            } catch (e : SocketTimeoutException) {
                _sessionState.update { SessionState.Error("Unable to connect to the server", R.drawable.ic_not_connecting) }
            } catch (e: Exception) {
                _sessionState.update { SessionState.Error("There has been a problem in the app", R.drawable.ic_error) }
            }
        }
    }
}