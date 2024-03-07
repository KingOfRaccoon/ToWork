package model

import kotlinx.serialization.Serializable

@Serializable
data class TrackWithModules(
    val id: Int,
    val name: String,
    val quantityModules: Int,
    val idLastCompleteModule: Int,
    val listModules: List<Module>
)