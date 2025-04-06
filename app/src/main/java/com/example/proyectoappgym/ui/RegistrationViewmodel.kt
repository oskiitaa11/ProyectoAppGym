package com.example.proyectoappgym.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import kotlinx.coroutines.launch

class RegistrationViewmodel(val userDatabase: RepositoryUserDatabase): ViewModel() {
    fun userExist(username: String): Boolean {
        var isExist = false

        viewModelScope.launch {
             isExist = userDatabase.userExist(username)
        }

        return isExist
    }
}