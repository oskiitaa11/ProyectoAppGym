package com.example.proyectoappgym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewmodel(val userDatabase: RepositoryUserDatabase): ViewModel() {
    var emailExist: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var userExist: MutableStateFlow<Boolean?> = MutableStateFlow(null)

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

    fun setEmailExistToNull() {
        emailExist.update { null }
    }

    fun setUserExistToNull() {
        userExist.update { null }
    }
}