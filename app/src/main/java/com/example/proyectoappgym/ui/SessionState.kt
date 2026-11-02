package com.example.proyectoappgym.ui

sealed interface SessionState {
    data object Loading: SessionState

    data object Authenticated: SessionState

    data object Unauthenticated: SessionState

    data class Error(val error : String, val idIcon: Int): SessionState
}