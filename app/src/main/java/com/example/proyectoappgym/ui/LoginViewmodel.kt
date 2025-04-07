package com.example.proyectoappgym.ui

import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.Flow


class LoginViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var intCompletedSignIn: MutableStateFlow<Int> = MutableStateFlow(0)

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
           intCompletedSignIn.update { userDatabase.signIn(username, password) }
        }
    }

    fun isCorrectPassword(username: String, password: String): Boolean {
        var isError = false

        viewModelScope.launch {
            isError = userDatabase.isCorrectPassword(username, password)
        }

        return isError
    }
}