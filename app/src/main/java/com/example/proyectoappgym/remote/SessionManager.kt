package com.example.proyectoappgym.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.proyectoappgym.entity.data.TokenResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val tokenResponse by lazy {

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
            preferences[EXPIRED_AT] = (tokenResponse.expiresIn + System.currentTimeMillis()).toString()
            preferences[EXPIRES_IN] = tokenResponse.expiresIn.toString()
        }
    }

    suspend fun getCurrentToken(): String? {
        return dataStore.data.map { preferences -> preferences[ACCESS_TOKEN] }.firstOrNull()
    }

    suspend fun clearSession() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    suspend fun getExpiredAt(): Long? {
        return dataStore.data.map { preferences -> preferences[EXPIRED_AT] }.firstOrNull()?.toLongOrNull()
    }

    suspend fun getRefreshToken(): String? {
        return dataStore.data.map { preferences -> preferences[REFRESH_TOKEN] }.firstOrNull()
    }

    suspend fun getExpiresIn(): Int? {
        return dataStore.data.map { preferences -> preferences[REFRESH_TOKEN] }.firstOrNull().toIntOrNull()
    }
}