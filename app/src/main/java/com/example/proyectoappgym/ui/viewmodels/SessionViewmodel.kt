package com.example.proyectoappgym.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.entity.data.TokenResponse
import com.example.proyectoappgym.remote.SessionManager
import com.example.proyectoappgym.ui.SessionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewmodel @Inject constructor(private val sessionManager: SessionManager): ViewModel() {
    var sessionState by mutableStateOf<SessionState>(SessionState.Loading)
        private set

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val token: TokenResponse? = sessionManager.tokenResponse.collect { it }
            val expiredAt = sessionManager.getExpiredAt()

            sessionManager.verifyToken(token, expiredAt)
            sessionState = if (token != null) SessionState.Authenticated else SessionState.Unauthenticated
        }
    }
}