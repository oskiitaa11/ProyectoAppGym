package com.example.proyectoappgym.db.db_users

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.proyectoappgym.entity.data.DataUserRequest
import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.users.User
import com.google.android.play.core.integrity.d
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


class UserRepository @Inject constructor(private val userApiService: UserApiService) {

    private var uidLoggedUser: String? = null

    fun updateUidLoggedUser(newUid: String?) {
        uidLoggedUser = newUid
    }

    fun getUidLoggedUser(): String? {
        return uidLoggedUser
    }

    suspend fun updateCurrentUser(dataUserRequest: DataUserRequest): MutableStateFlow<User> {
        return userApiService.updateCurrentUser(dataUserRequest)
    }

    suspend fun getCurrentUser(): MutableStateFlow<User?> {
        return userApiService.getCurrentUser()
    }
}

