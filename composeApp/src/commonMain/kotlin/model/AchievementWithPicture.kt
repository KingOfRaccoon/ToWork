package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AchievementWithPicture(
    val id: Int,
    val name: String,
    val desc: String,
    @SerialName("picture") val imageUrl: String
)
