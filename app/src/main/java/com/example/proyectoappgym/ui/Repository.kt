package com.example.proyectoappgym.ui

import com.example.proyectoappgym.entity.Question

interface Repository {
    suspend fun getAllQuestions(): List<Question>
}