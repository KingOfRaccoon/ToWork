package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserWithProgress(
    val id: Int,
    @SerialName("name") val login: String,
    @SerialName("last_name") val fullName: String,
    val progress: Int
)