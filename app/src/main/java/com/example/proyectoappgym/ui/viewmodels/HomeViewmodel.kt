package com.example.proyectoappgym.ui.viewmodels

import android.R.attr.apiKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.Question
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var currentUser = MutableStateFlow(User())
    var stringRoutines = MutableStateFlow("")

    init {
        viewModelScope.launch {
            userDatabase.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }

    fun getRoutines(answeredQuestions: Map<String, List<String>>) {
        viewModelScope.launch(Dispatchers.IO) {
            //val c = userDatabase.saveUserTrainingRoutinesGpt(answeredQuestions)
        }
    }
}