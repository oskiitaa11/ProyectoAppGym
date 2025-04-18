package com.example.proyectoappgym.db.db_users

import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow

interface RepositoryUserDatabase {
    suspend fun addUser(user: User): Boolean
    suspend fun deleteUser(user: User): String
    suspend fun signIn(email: String, password: String): Int
    suspend fun signOut()
    suspend fun getCurrentUser(): User?
    suspend fun emailExist(email: String): Boolean?
    suspend fun userExist(username: String): Boolean?
    suspend fun authWithGoogle(idToken: String): Boolean
}