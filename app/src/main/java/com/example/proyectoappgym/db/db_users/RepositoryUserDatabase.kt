package com.example.proyectoappgym.db.db_users

import android.net.Uri
import com.example.proyectoappgym.db.retrofit.entity.ExercisesName
import com.example.proyectoappgym.db.retrofit.entity.Routines
import com.example.proyectoappgym.entity.Avatars
import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.TrainingRoutine
import com.example.proyectoappgym.entity.User
import com.google.android.gms.common.api.internal.ApiKey
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface RepositoryUserDatabase {
    suspend fun addUser(user: User): Boolean
    suspend fun deleteUser(user: User): String
    suspend fun signIn(email: String, password: String): Int
    suspend fun signOut()
    suspend fun updateCurrentUser()
    suspend fun getCurrentUser(): MutableStateFlow<User?>
    suspend fun updateNameCurrentUser(newName: String)
    suspend fun updateAvatarProfile(avatar: Avatars)
    suspend fun updateResponses(question: String, newResponses: List<String>)
    suspend fun removeResponsesOfQuestion(question: String)
    suspend fun createRoutine(newAnsweredQuestion: Map<String, List<String>>): List<TrainingRoutine>
    suspend fun emailExist(email: String): Boolean?
    suspend fun userExist(username: String): Boolean?
    suspend fun authWithGoogle(idToken: String): Boolean
    suspend fun changeWeeklyRoutine(newAnsweredQuestion: Map<String, List<String>>): Boolean
}