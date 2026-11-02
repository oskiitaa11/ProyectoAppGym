package com.example.proyectoappgym.db.db_users

import com.example.proyectoappgym.entity.data.request.DataUserRequest
import com.example.proyectoappgym.entity.data.request.UpdateEmailRequest
import com.example.proyectoappgym.entity.data.request.UpdatePasswordRequest
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.remote.SessionManager
import javax.inject.Inject


class UserRepository @Inject constructor(private val userApiService: UserApiService, private val sessionManager: SessionManager) {

    suspend fun updateCurrentUser(dataUserRequest: DataUserRequest): User {
        val response = userApiService.updateCurrentUser(dataUserRequest)

        if (!response.isSuccessful || response.body() == null) {
            throw ErrorException.fromJson(response.errorBody()?.string() ?: "",
                ErrorException("user_not_updated", "The user haven't updated, please try again"))
        }

        return response.body() as User
    }

    suspend fun getCurrentUser(): User {
        val response = userApiService.getCurrentUser()

        if (!response.isSuccessful || response.body() == null) {
            throw ErrorException.fromJson(response.errorBody()?.string() ?: "",
                ErrorException("user_not_found", "The user haven't found, please logging again"))
        }

        return response.body() as User
    }

    suspend fun logout() {
        userApiService.logout()
        sessionManager.clearSession()
    }

    suspend fun deleteCurrentUser() {
        userApiService.deleteCurrentUser()
        sessionManager.clearSession()
    }

    suspend fun updatePassword(actualPassword: String, newPassword: String) {
        val request = UpdatePasswordRequest(actualPassword, newPassword)
        val response = userApiService.updatePassword(request)

        if (!response.isSuccessful) {
            throw ErrorException.fromJson(response.errorBody()?.string() ?: "",
                ErrorException("error_while_password_updated", "There has been while the password was updating"))
        }

    }

    suspend fun updateEmail(newEmail: String) {
        val request = UpdateEmailRequest(newEmail)
        val response = userApiService.updateEmail(request)

        if (!response.isSuccessful) {
            throw ErrorException.fromJson(response.errorBody()?.string() ?: "",
                ErrorException("error_while_email_updated", "There has been while the email was updating"))
        }

    }
}

