package com.example.proyectoappgym.db.db_questions

import com.example.proyectoappgym.entity.Question

interface RepositoryQuestions {
    suspend fun allQuestions(): List<Question>
}