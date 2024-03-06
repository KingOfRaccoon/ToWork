package model

import kotlinx.serialization.Serializable

@Serializable
data class Knowledge(val id: Int, val name: String, val content: String)
