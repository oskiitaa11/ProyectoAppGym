package com.example.proyectoappgym.db.db_questions

import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType

object QuestionsRegistration: RepositoryQuestions {
    val allQuestions = listOf(
        Question(
            question = "¿Eres más de ejercicios de calistenia o gym?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf("Calistenia", "Gym", "De los dos").toTypedArray()
        ),
        Question(
            question = "¿En qué tipos de ejercicios de Gym te enfocas más o te quieres enfocar?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf("Ejercicios con máquinas", "Ejercicios de levantamiento de pesas").toTypedArray()

        ),
        Question(
            question = "¿En qué tipos de ejercicios de Calistenia te enfocas más o te quieres enfocar?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Ejercicios de tensión", "Ejercicios básicos").toTypedArray()

        ),
        Question(
            question = "¿Cuáles son tus objetivos?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Conseguir más fuerza", "Conseguir más resistencia", "Ganar más músculo").toTypedArray()
        ),
        Question(
            question = "¿Estás haciendo alguna dieta?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Sí, para ganar más masa muscular", "Sí, para perder grasa corporal", "Sí, para mantenerme", "No").toTypedArray()
        ),
        Question(
            question = "¿Cuánto tiempo llevas entrenando?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf("Acabo de empezar con MyFitnessApp", "Llevo unos meses", "Llevo 1 año o más").toTypedArray()
        ),
        Question(
            question = "¿Qué días de la semana puedes/quieres entrenar?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo").toTypedArray()
        )
    )

    override suspend fun allQuestions(): List<Question> {
        return allQuestions
    }
}