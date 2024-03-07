package model

import kotlinx.serialization.Serializable

@Serializable
data class AnswerBot(val query: String, val result: String)
