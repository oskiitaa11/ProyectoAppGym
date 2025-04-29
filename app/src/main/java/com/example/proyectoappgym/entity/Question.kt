package com.example.proyectoappgym.entity

import com.google.android.gms.common.api.Response

private var _id = 1
class Question(val id: Int = _id++, val question: String, val responsesTypes: ResponsesType, vararg val responses: String)