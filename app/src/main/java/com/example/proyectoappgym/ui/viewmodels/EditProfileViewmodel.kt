package com.example.proyectoappgym.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.entity.User
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.model.DocumentKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditProfileViewmodel(private val userDatabase: RepositoryUserDatabase): ViewModel() {
    val currentUser = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            currentUser.update { userDatabase.getCurrentUser()!! }
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            userDatabase.updateNameCurrentUser(newName)
        }
    }

    fun updateAvatarProfile(uri: Uri, currentUser: User) {
        viewModelScope.launch {
            userDatabase.updateProfileAvatar(uri, currentUser)
        }
    }
}