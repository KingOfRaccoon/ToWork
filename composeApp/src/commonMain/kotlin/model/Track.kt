package model

import kotlinx.serialization.Serializable

@Serializable
data class Track(val id: Int, val name: String, val quantityModules: Int)