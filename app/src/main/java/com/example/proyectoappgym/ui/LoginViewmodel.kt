package com.example.proyectoappgym.ui

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


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