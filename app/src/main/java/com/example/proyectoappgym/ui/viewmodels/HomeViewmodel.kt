package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.UserApiService
import com.example.proyectoappgym.db.db_users.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    var currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            userRepository.getCurrentUser().collect { user -> currentUser.value = user ?: User() }
        }
    }

}