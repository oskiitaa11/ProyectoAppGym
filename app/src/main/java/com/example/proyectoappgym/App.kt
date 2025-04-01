package com.example.proyectoappgym

import android.app.Application
import com.example.proyectoappgym.db_questions.QuestionsRegistration
import com.example.proyectoappgym.db_questions.RepositoryQuestions

class App: Application() {
    val repositoryQuestions: RepositoryQuestions by lazy { QuestionsRegistration }
}