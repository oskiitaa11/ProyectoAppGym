package com.example.proyectoappgym.db.retrofit.entity

data class ChatRequest(val model: String = "gpt-4o", val messages: List<ChatMessage>)