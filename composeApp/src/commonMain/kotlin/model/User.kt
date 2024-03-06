package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("access_token") val token: String,
    @SerialName("id_user") val id: Int,
    @SerialName("name_user") val login: String,
    @SerialName("last_name_user") val fullName: String,
    @SerialName("progress_user") val progress: Int,
)
