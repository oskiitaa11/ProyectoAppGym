package com.example.proyectoappgym.db.retrofit.entity

data class ChatRequest(val model: String = "deepseek-chat", val message: List<ChatMessage>)