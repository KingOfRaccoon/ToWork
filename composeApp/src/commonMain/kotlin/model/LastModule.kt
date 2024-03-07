package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LastModule(
    @SerialName("id_module") val idTrack: Int,
    @SerialName("id_track") val idModule: Int,
    @SerialName("number_last_complete_page") val lastPage: Int
)
