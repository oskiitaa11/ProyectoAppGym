package com.example.proyectoappgym.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_auth.AuthRepository
import com.example.proyectoappgym.db.db_users.UserRepository
import com.example.proyectoappgym.entity.users.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class LoginViewmodel @Inject constructor(private val authRepository: AuthRepository): ViewModel() {
    var intCompletedSignIn: MutableStateFlow<Int> = MutableStateFlow(0)
    val interceptor =

    fun signIn(password: String, email: String) {
        viewModelScope.launch {
            val authUser = mapOf<String, Any>("password" to password as Object, "email" to email)
            val tokenResponse = authRepository.signIn(authUser)

           //intCompletedSignIn.update { authRepository.signIn(username, password).value }
        }
    }

    fun setNumberCompletedSignInToZero() {
        intCompletedSignIn.update { 0 }
    }
}