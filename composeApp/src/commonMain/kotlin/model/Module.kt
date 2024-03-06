package model

import kotlinx.serialization.Serializable

@Serializable
data class Module(
    val id: Int,
    val name: String,
    val quantityPages: Int,
    val quantityCoins: Int,
    val startContent: String,
    val endContent: String,
    val numberInTrack: Int,
    val idTrack: Int
)
