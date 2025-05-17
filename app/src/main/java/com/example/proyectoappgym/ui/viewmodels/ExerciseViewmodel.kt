package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ExerciseViewmodel(userDatabase: RepositoryUserDatabase): ViewModel() {
    var currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            userDatabase.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }

}