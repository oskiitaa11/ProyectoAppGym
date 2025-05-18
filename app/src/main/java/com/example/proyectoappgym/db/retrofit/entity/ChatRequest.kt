package com.example.proyectoappgym.db.retrofit.entity

data class ChatRequest(val model: String = "gpt-4.1", val messages: List<ChatMessage>)