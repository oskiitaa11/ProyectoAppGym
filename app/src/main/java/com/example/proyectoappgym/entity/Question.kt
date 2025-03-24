package com.example.proyectoappgym.entity

import com.google.android.gms.common.api.Response

class Question(val question: String, val responsesTypes: ResponsesType, vararg val responses: String)