package com.example.proyectoappgym.db_questions

import com.example.proyectoappgym.entity.Question

interface RepositoryQuestions {
    suspend fun allQuestions(): List<Question>
}