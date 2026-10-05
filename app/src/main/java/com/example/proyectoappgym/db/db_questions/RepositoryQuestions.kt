package com.example.proyectoappgym.db.db_questions

import com.example.proyectoappgym.entity.questions.Question

interface RepositoryQuestions {
    fun allQuestions(): List<Question>
}