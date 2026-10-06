package com.example.proyectoappgym.remote

import android.annotation.SuppressLint
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.proyectoappgym.db.db_auth.AuthApiService
import com.example.proyectoappgym.entity.data.TokenResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Dispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val dataStore: DataStore<Preferences>, private val authApiService: Lazy<AuthApiService>) {

    val tokenResponse: StateFlow<TokenResponse?> = dataStore.data.map {
        if (it[ACCESS_TOKEN] != null) {
            TokenResponse(it[ACCESS_TOKEN].toString(),
                it[REFRESH_TOKEN].toString(), it[EXPIRES_IN]?.toInt() ?: 0)
        } else null
    }.stateIn(scope = CoroutineScope(Dispatchers.IO), started = SharingStarted.WhileSubscribed(5000), initialValue = null)

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
            preferences[EXPIRED_AT] = (tokenResponse.expiresIn + System.currentTimeMillis()).toString()
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
        var tokenResponse: TokenResponse? = authApiService.value.refreshToken(refreshToken).firstOrNull()
        if (tokenResponse != null)
            saveAccessToken(tokenResponse)
        else throw IllegalArgumentException("There has been a problem with user session")
    }

    /*Preguntar dos veces por el token antes y despues de entrar en el synchronized,
      permite que dos hilos no modifiquen el token dos veces y de error
      cuando el segundo hilo va a ejecutar*/
    fun verifyToken(tokenResponse: TokenResponse?, expiredAt: Long?) {
        val newToken: TokenResponse?
        val newExpiredAt: Long?

        if (tokenResponse != null && expiredAt != null)
            synchronized(this) {
                newToken = tokenResponse
                newExpiredAt = runBlocking { getExpiredAt() }
                if (isTokenExpired(tokenResponse.expiresIn, expiredAt))
                    runBlocking {
                        refreshToken(tokenResponse.refreshToken)
                    }
                else throw IllegalArgumentException("Error of session")
            }
    }

    private fun isTokenExpired(expiresIn: Int, expiredAt: Long): Boolean {
        return System.currentTimeMillis() + expiresIn >= expiredAt - 60
    }
}