package com.example.proyectoappgym.db.retrofit.entity

data class ChatRequest(val model: String = "gpt-4-turbo", val messages: List<ChatMessage>)