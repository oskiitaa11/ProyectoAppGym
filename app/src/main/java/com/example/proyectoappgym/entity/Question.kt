package com.example.proyectoappgym.entity

import kotlinx.serialization.Serializable

private var _id = 1

@Serializable
class Question(val id: Int = _id++, val question: String, val responsesTypes: ResponsesType, vararg val responses: String)