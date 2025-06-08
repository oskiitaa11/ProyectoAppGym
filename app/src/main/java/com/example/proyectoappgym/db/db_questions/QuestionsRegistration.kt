package com.example.proyectoappgym.db.db_questions

import com.example.proyectoappgym.entity.Question
import com.example.proyectoappgym.entity.ResponsesType

object QuestionsRegistration: RepositoryQuestions {
    val allQuestions = listOf(
        Question(
            question = "Are you more into calisthenics or gym workouts?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf("Calisthenics", "Gym", "Both").toTypedArray()
        ),
        Question(
            question = "What are your goals?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Gain more strength", "Increase endurance", "Build more muscle").toTypedArray()
        ),
        Question(
            question = "What types of gym exercises do you focus on or want to focus on?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Machine exercises", "Weightlifting exercises").toTypedArray()
        ),
        Question(
            question = "What types of calisthenics exercises do you focus on or want to focus on?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf("Tension exercises", "Basic exercises").toTypedArray()
        ),
        Question(
            question = "Are you following any diet?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf(
                "Yes, to gain muscle mass",
                "Yes, to lose body fat",
                "Yes, to maintain",
                "No"
            ).toTypedArray()
        ),
        Question(
            question = "How long have you been training?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            responses = listOf(
                "I just started with MyFitnessApp",
                "I've been training for a few months",
                "I've been training for a year or more"
            ).toTypedArray()
        ),
        Question(
            question = "Which days of the week can/do you want to train?",
            responsesTypes = ResponsesType.CHECKBOX,
            responses = listOf(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            ).toTypedArray()
        )
    )

    override fun allQuestions(): List<Question> {
        return allQuestions
    }
}