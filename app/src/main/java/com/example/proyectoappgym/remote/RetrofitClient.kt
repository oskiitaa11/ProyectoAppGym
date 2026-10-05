package com.example.proyectoappgym.remote

import com.example.proyectoappgym.db.db_auth.AuthApiService
import com.example.proyectoappgym.db.db_routines.RoutineApiService
import com.example.proyectoappgym.db.db_users.UserApiService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://localhost:8080"
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    val userApi: UserApiService by lazy {
        retrofit.create(UserApiService::class.java)
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }

    val routineApi: RoutineApiService by lazy {
        retrofit.create(RoutineApiService::class.java)
    }

}