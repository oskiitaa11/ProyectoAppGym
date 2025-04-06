package com.example.proyectoappgym.db_users

import com.example.proyectoappgym.entity.User
import kotlinx.coroutines.flow.MutableStateFlow

interface RepositoryUserDatabase {
    suspend fun addUser(user: User): Boolean
    suspend fun deleteUser(user: User): String
    suspend fun signIn(username: String, password: String): Int
    suspend fun signOut()
    suspend fun userExist(username: String): Boolean
    suspend fun isCorrectPassword(username: String, password: String): Boolean
}