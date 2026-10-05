package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    var currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            currentUser = userRepository.getCurrentUser() as MutableStateFlow<User>
        }
    }
}