package com.example.proyectoappgym.db_questions

import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType

object QuestionsRegistration: RepositoryQuestions {
    val allQuestions = listOf(
        Question("¿Eres más de ejercicios de calistenia o gym?", ResponsesType.RADIOBUTTON, "Calistenia", "Gym", "De los dos"),
        Question("¿En qué tipos de ejercicios de Gym te enfocas más o te quieres enfocar?",  ResponsesType.RADIOBUTTON, "Ejercicios con máquinas", "Ejercicios de levantamiento de pesas"),
        Question("¿En qué tipos de ejercicios de Calistenia te enfocas más o te quieres enfocar?",  ResponsesType.CHECKBOX, "Ejercicios de tension", "Ejercicios básicos"),
        Question("¿Cuáles son tus objetivos?", ResponsesType.CHECKBOX, "Conseguir mas fuerza", "Conseguir mas resistencia", "Ganar mas musculo"),
        Question("¿Estas haciendo alguna dieta?", ResponsesType.CHECKBOX, "¿Si, para ganar mas masa muscular?", "Si, para perder grasa corporal", "Si, para mantenerme", "No"),
        Question("¿Cuánto tiempo llevas entrenando?", ResponsesType.RADIOBUTTON, "Acabo de empezar con MyFitnessApp", "Llevo unos meses", "Llevo 1 año o más"),
        Question("¿Qué dias de la semana puedes/quieres entrenar?", ResponsesType.CHECKBOX,"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"),
    )

    override suspend fun allQuestions(): List<Question> {
        return allQuestions
    }
}