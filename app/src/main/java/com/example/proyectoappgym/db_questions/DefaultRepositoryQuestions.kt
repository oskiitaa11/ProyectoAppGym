package com.example.proyectoappgym.db_questions

import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.ui.Repository

class DefaultRepositoryQuestions(val allQuestions: List<Question>): Repository {
    override suspend fun getAllQuestions(): List<Question> {
        return allQuestions
    }

}