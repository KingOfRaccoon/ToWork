package model

import kotlinx.serialization.Serializable

@Serializable
data class Achievement(val id: Int, val name: String, val desc: String)
