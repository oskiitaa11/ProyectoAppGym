package com.example.proyectoappgym

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.proyectoappgym.db.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db.db_users.UserRepository
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltAndroidApp
class App: Application() {
    val repositoryQuestions: RepositoryQuestions by lazy { QuestionsRegistration }
    private val sessionManager =
    private val datastore: DataStore<Preferences> by preferencesDataStore(name = "user_session")
    val isLoggedUser: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    override fun onCreate() {
        super.onCreate()
        val preferenceKey = stringPreferencesKey("user_token")

        //FirebaseApp.initializeApp(this)
        //userDatabase.initializerApp()
        GlobalScope.launch {
            changeUidLoggedUser()
            /*userDatabase.updateUidLoggedUser(datastore.data.first()[preferenceKey])
            if(isLoggedUser.value as Boolean) userDatabase.updateCurrentUser()*/
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
        isLoggedUser.update { datastore.data.first()[preferenceKey]?.isNotEmpty() ?: false }
    }

    fun addLoggedUserFromMain(idToken: String) {
        GlobalScope.launch {
            addLoggedUser(idToken)
            changeUidLoggedUser()
            //userDatabase.updateCurrentUserAfterLogin()
        }
    }

    suspend fun logoutUser() {
        val preferenceKey = stringPreferencesKey("user_token")

        datastore.edit { preferences ->
            preferences[preferenceKey] = ""
        }
    }
}