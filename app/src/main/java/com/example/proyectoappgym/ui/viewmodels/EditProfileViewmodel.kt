package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    val currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            currentUser.update { userDatabase.getCurrentUser()!! }
        }
    }
}