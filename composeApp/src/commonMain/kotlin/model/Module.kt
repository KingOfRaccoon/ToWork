package model

import kotlinx.serialization.Serializable

@Serializable
data class Module(
    val id: Int,
    val name: String,
    val quantityPage: Int = 0,
    val quantityCoin: Int = 0,
    val startContent: String,
    val endContent: String,
    val numberInTrack: Int,
    val idTrack: Int,
    var completePages: Int = 0
)
