package com.example.proyectoappgym.remote

import android.annotation.SuppressLint
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.proyectoappgym.db.db_auth.AuthApiService
import com.example.proyectoappgym.db.db_users.UserApiService
import com.example.proyectoappgym.entity.data.request.RefreshTokenRequest
import com.example.proyectoappgym.entity.data.request.TokenResponse
import com.example.proyectoappgym.entity.exceptions.ErrorException
import dagger.Lazy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val dataStore: DataStore<Preferences>, private val authApiService: Lazy<AuthApiService>) {
    val tokenResponse: Flow<TokenResponse?> = dataStore.data.map {
        if (it[ACCESS_TOKEN] != null && it[REFRESH_TOKEN] != null) {
            TokenResponse(it[ACCESS_TOKEN].toString(),
                it[REFRESH_TOKEN].toString(), it[EXPIRES_IN]?.toInt() ?: 0)
        } else null
    }

    companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val EXPIRED_AT = stringPreferencesKey("expired_at")
        val EXPIRES_IN = stringPreferencesKey("expires_in")
    }

    suspend fun saveAccessToken(tokenResponse: TokenResponse) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = tokenResponse.accessToken
            preferences[REFRESH_TOKEN] = tokenResponse.refreshToken
            preferences[EXPIRED_AT] = (tokenResponse.expiresIn + (System.currentTimeMillis() / 1000)).toString()
            preferences[EXPIRES_IN] = tokenResponse.expiresIn.toString()
        }


    }

    suspend fun clearSession() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    suspend fun getExpiredAt(): Long? {
        return dataStore.data.map { preferences -> preferences[EXPIRED_AT] }.firstOrNull()?.toLongOrNull()
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun refreshToken(refreshToken: String) {
        val refreshTokenRequest = RefreshTokenRequest(refreshToken)
        val tokenResponse = authApiService.get().refreshToken(refreshTokenRequest)
        val token: TokenResponse? = tokenResponse.body()

        if (tokenResponse.isSuccessful) {
            if (token != null)
                saveAccessToken(token)
            else throw ErrorException("a_problem_with_session_user","There has been a problem with user session")
        } else {
            throw ErrorException.fromJson(tokenResponse.errorBody().toString(),
                ErrorException("a_problem_with_session_user","There has been a problem with user session"))
        }
    }

    /*Preguntar dos veces por el token antes y despues de entrar en el synchronized,
      permite que dos hilos no modifiquen el token dos veces y de error
      cuando el segundo hilo va a ejecutar*/
    suspend fun verifyToken(tokenResponse: TokenResponse?, expiredAt: Long?) {
        var newToken: TokenResponse?
        val newExpiredAt: Long?

        if (tokenResponse != null && expiredAt != null) {
            newToken = tokenResponse
            newExpiredAt = getExpiredAt() ?: 0
            if (isTokenExpired(newExpiredAt))
                refreshToken(newToken.refreshToken)
        }
    }

    private fun isTokenExpired(expiredAt: Long): Boolean {
        val actualTimeInSeconds = System.currentTimeMillis() / 1000
        return actualTimeInSeconds >= expiredAt - 60
    }

    suspend fun getValidToken(): TokenResponse? {
        val currentToken = tokenResponse.firstOrNull()
        val expiredAt = getExpiredAt()

        if (expiredAt != null && currentToken != null && isTokenExpired(expiredAt)) {
            refreshToken(currentToken.refreshToken)
        }

        return tokenResponse.firstOrNull()
    }
}