package com.example.proyectoappgym.db.retrofit.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.checkerframework.checker.index.qual.SearchIndexBottom

data class Output(
    @SerializedName("content")
    @Expose
    private val content: List<Content>
)