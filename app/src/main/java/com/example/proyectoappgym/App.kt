package com.example.proyectoappgym

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.db.db_users.UserDatabase
import com.example.proyectoappgym.entity.User
import com.google.api.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser


import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class App: Application() {
    val repositoryQuestions: RepositoryQuestions by lazy { QuestionsRegistration }
    val userDatabase by lazy { UserDatabase() }
    private val datastore: DataStore<Preferences> by preferencesDataStore(name = "user_token")
    val isLoggedUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    var uidLoggedUser: String? = null

    override fun onCreate() {
        super.onCreate()
        val preferenceKey = stringPreferencesKey("user_token")

        FirebaseApp.initializeApp(this)
        userDatabase.initializerApp()
        GlobalScope.launch {
            changeUidLoggedUser()
            uidLoggedUser = datastore.data.first()[preferenceKey]
            userDatabase.updateUidLoggedUser(uidLoggedUser)
        }
    }

    private suspend fun addLoggedUser(idToken: String) {
        val preferenceKey = stringPreferencesKey("user_token")

        datastore.edit { preferences ->
            preferences[preferenceKey] = idToken
        }
    }

    private suspend fun changeUidLoggedUser(){
        val preferenceKey = stringPreferencesKey("user_token")
        isLoggedUser.update { datastore.data.first().get(preferenceKey)?.isNotEmpty() ?: false }
    }

    fun addLoggedUserFromMain(idToken: String) {
        GlobalScope.launch {
            addLoggedUser(idToken)
            changeUidLoggedUser()
        }
    }
}