package model

import kotlinx.serialization.Serializable

@Serializable
data class ChatMessage(val id: Int, val isUser: Boolean, val message: String)
