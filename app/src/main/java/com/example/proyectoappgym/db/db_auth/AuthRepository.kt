package com.example.proyectoappgym.db.db_auth

import com.example.proyectoappgym.entity.data.request.FullUserRequest
import com.example.proyectoappgym.entity.data.request.RefreshTokenRequest
import com.example.proyectoappgym.entity.data.request.RequestExistingUsername
import com.example.proyectoappgym.entity.data.request.ResponseExistingUser
import com.example.proyectoappgym.entity.data.request.SignInUser
import com.example.proyectoappgym.entity.data.request.TokenResponse
import com.example.proyectoappgym.entity.exceptions.ErrorException
import com.example.proyectoappgym.entity.users.User
import com.example.proyectoappgym.remote.SessionManager
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class AuthRepository @Inject constructor(private val authApiService: AuthApiService, private val sessionManager: SessionManager) {

    suspend fun signUp(user: User) {
        val fullUserRequest = FullUserRequest.getFullUserRequestFromUser(user)
        val response = authApiService.signUp(fullUserRequest)
        val exception: ErrorException

        if (!response.isSuccessful) {
            exception = ErrorException.fromJson(response.errorBody()?.string() ?: "", ErrorException("request_rejected", "The request has been rejected"))

            when {
                response.code() == 401 || exception.error == "email_already_exists"  -> throw exception
                else -> throw ErrorException("user_not_created", "There was a problem creating the user")
            }
        }
    }

    suspend fun signIn(authUser: SignInUser) {
        var tokenResponse: TokenResponse?
        val response = authApiService.signIn(authUser)

        if (response.isSuccessful) {
            tokenResponse = response.body()
            if (tokenResponse != null)
                sessionManager.saveAccessToken(tokenResponse)
            else throw ErrorException("request_rejected", "The request has been rejected")
        } else {
            throw ErrorException.fromJson(response.errorBody()?.string() ?: "",
                ErrorException("error_for_signing_in_user", "There has been while the user logging in"))
        }
    }

    suspend fun existsUser(username: String, idForClient: Long?): Response<ResponseExistingUser> {
        val request = RequestExistingUsername(username, idForClient)
        return authApiService.existsUser(request)
    }
}