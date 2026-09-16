package com.example.proyectoappgym.entity

import kotlinx.serialization.Serializable
import okhttp3.Response

private var _id = 1

@Serializable
class Question(val id: Int = _id++, val question: String, val responsesTypes: ResponsesType, private vararg var _responses: String) {

    val responses: Array<out String>
        get() = _responses

    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as Question

        if (id != other.id) return false
        if (question != other.question) return false
        if (responsesTypes != other.responsesTypes) return false
        if (!responses.contentEquals(other.responses)) return false // ¡Clave para Arrays!

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + question.hashCode()
        result = 31 * result + responsesTypes.hashCode()
        result = 31 * result + responses.contentHashCode() // ¡Clave para Arrays!

        return result
    }

    fun copy(): Question {
        return Question(id, question, responsesTypes, *responses.toList().toTypedArray())
    }

    fun addResponse(response: String) {
        var actualResponse = responses.toMutableList()

        if(_responses.any { it == response }) return
        actualResponse.add(response)
        _responses = actualResponse.toTypedArray()
    }

    fun removeResponse(response: String) {
        var actualResponse = responses.toMutableList()

        if(!_responses.any { it == response }) return
        actualResponse.remove(response)
        _responses = actualResponse.toTypedArray()
    }
}