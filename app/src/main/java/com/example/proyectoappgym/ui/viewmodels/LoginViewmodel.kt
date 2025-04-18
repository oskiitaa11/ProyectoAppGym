package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class LoginViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var intCompletedSignIn: MutableStateFlow<Int> = MutableStateFlow(0)
    var isSuccessfulGoogleAuth: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
           intCompletedSignIn.update { userDatabase.signIn(username, password) }
        }
    }

    fun authWithGoogle(idToken: String) {
        viewModelScope.launch {
            isSuccessfulGoogleAuth.update { userDatabase.authWithGoogle(idToken) }
        }
    }

    fun setNumberCompletedSignInToZero() {
        intCompletedSignIn.update { 0 }
    }
}