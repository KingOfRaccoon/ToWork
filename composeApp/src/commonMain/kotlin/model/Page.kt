package model

import kotlinx.serialization.Serializable

@Serializable
data class Page(
    val id: Int,
    val content: String,
    val name: String,
    val numberInModule: Int,
    val idModule: Int
)
