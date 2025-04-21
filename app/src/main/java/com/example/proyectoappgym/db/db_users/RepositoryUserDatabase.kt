package com.example.proyectoappgym.db.db_users

import android.net.Uri
import com.example.proyectoappgym.entity.User
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow

interface RepositoryUserDatabase {
    suspend fun addUser(user: User): Boolean
    suspend fun deleteUser(user: User): String
    suspend fun signIn(email: String, password: String): Int
    suspend fun signOut()
    suspend fun getCurrentUser(): User?
    suspend fun updateNameCurrentUser(newName: String)
    suspend fun updateProfileAvatar(uri: Uri, currentUser: User)
    suspend fun emailExist(email: String): Boolean?
    suspend fun userExist(username: String): Boolean?
    suspend fun authWithGoogle(idToken: String): Boolean
}