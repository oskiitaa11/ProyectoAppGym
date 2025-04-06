package com.example.proyectoappgym

import android.app.Application
import androidx.compose.ui.platform.LocalContext
import com.example.proyectoappgym.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db_questions.RepositoryQuestions
import com.example.proyectoappgym.db_users.RepositoryUserDatabase
import com.example.proyectoappgym.db_users.UserDatabase
import com.google.firebase.FirebaseApp

class App: Application() {
    val repositoryQuestions: RepositoryQuestions by lazy { QuestionsRegistration }
    val userDatabase by lazy { UserDatabase() }

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        userDatabase.initializerApp()
    }
}