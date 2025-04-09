package com.example.proyectoappgym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewmodel(val userDatabase: RepositoryUserDatabase): ViewModel() {
    var emailExist = MutableStateFlow(false)
    var userExist = MutableStateFlow(false)

    fun userExist(username: String) {
        viewModelScope.launch {
             userExist.update { userDatabase.userExist(username) }
        }
    }

    fun emailExist(email: String) {
        viewModelScope.launch {
            emailExist.update { userDatabase.emailExist(email) }
        }

    }
}