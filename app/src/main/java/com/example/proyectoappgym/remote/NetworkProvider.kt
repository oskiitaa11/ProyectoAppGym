package com.example.proyectoappgym.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.proyectoappgym.db.db_auth.AuthApiService
import com.example.proyectoappgym.db.db_routines.RoutineApiService
import com.example.proyectoappgym.db.db_users.UserApiService
import com.example.proyectoappgym.entity.data.TokenResponse
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvider {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl("http://localhost:8080").client(okHttpClient).addConverterFactory(
            GsonConverterFactory.create()).build()
    }

    @Provides
    fun provideToken(dataStore: DataStore<Preferences>) {
        dataStore.data.map {  }
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApiService {
        return retrofit.create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoutineApi(retrofit: Retrofit): RoutineApiService {
        return retrofit.create(RoutineApiService::class.java)
    }
}