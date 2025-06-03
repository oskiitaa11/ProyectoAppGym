package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    var currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            userDatabase.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }

    fun createRoutine() {
        viewModelScope.launch {
            userDatabase.createRoutine(
                mapOf(
                    "Are you more into calisthenics or gym workouts?" to listOf("Gym"),
                    "What types of gym exercises do you focus on or want to focus on?" to listOf("Machine exercises", "Weightlifting exercises"),
                    "What types of calisthenics exercises do you focus on or want to focus on?" to listOf("Tension exercises", "Basic exercises"),
                    "What are your goals?" to listOf("Gain more strength", "Increase endurance", "Build more muscle"),
                    "Which days of the week can/do you want to train?" to listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
                )
            )
        }
    }
}