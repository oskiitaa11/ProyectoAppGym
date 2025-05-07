package com.example.proyectoappgym.db.retrofit

import android.os.Message
import com.example.proyectoappgym.db.retrofit.entity.ChatRequest
import com.example.proyectoappgym.db.retrofit.entity.ChatResponse
import com.example.proyectoappgym.entity.Question
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {
    @POST("v1/chat/completions")
    suspend fun getChatResponse(
        @Header("Authorization") auth: String,
        @Body request: ChatRequest
    ): ChatResponse
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl("https://api.openai.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api: OpenAIApi = retrofit.create(OpenAIApi::class.java)

suspend fun sendMessageToGPT(questions: List<Question>, apiKey: String): String {
    val message =
    val request = ChatRequest(
        message = message
    )

    val response = api.getChatResponse("Bearer $apiKey", request)
    return response.message
}

suspend fun buildFromAnsweredQuestions(questions: List<Question>) {
    var responsesString: String
    questions.map { question ->
        responsesString = question.responses.joinToString("\n")
        "$question\n${question.responses}"
    }
}