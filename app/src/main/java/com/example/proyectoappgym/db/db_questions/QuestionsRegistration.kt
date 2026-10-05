package com.example.proyectoappgym.db.db_questions

import com.example.proyectoappgym.entity.questions.Question
import com.example.proyectoappgym.entity.questions.ResponsesType

object QuestionsRegistration: RepositoryQuestions {
    val allQuestions = listOf(
        Question(
            question = "Are you more into calisthenics or gym workouts?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            _responses = listOf("Calisthenics", "Gym workouts", "Both").toTypedArray()
        ),
        Question(
            question = "What are your goals?",
            responsesTypes = ResponsesType.CHECKBOX,
            _responses = listOf("Gain more strength", "Build more muscle").toTypedArray()
        ),
        Question(
            question = "Do you work out at home or at the gym?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            _responses = listOf("With my equipments at home", "At the gym").toTypedArray()
        ),
        Question(
            question = "Where do you train calisthenics?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            _responses = listOf("At home", "At the calisthenic park").toTypedArray()
        ),
        Question(
            question = "Are you following any diet?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            _responses = listOf(
                "Yes, to gain muscle mass",
                "Yes, to lose body fat",
                "Yes, to maintain",
                "No"
            ).toTypedArray()
        ),
        Question(
            question = "How long have you been training?",
            responsesTypes = ResponsesType.RADIOBUTTON,
            _responses = listOf(
                "I just started with MyFitnessApp",
                "I've been training for a few months",
                "I've been training for a year or more"
            ).toTypedArray()
        ),
        Question(
            question = "Which days of the week can you train?",
            responsesTypes = ResponsesType.CHECKBOX,
            _responses = listOf(
                "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
            ).toTypedArray()
        )
    )

    override fun allQuestions(): List<Question> {
        return allQuestions
    }
}