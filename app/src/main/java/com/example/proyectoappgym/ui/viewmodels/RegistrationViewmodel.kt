package com.example.proyectoappgym.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.internal.dns.DnsMessage.Companion.response
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class RegistrationViewmodel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    private val userExists = MutableStateFlow(false)
    private val emailExists = MutableStateFlow(false)

    fun registerUser(user: User) {
        viewModelScope.launch {
            authRepository.signUp(user)

        }
    }
}