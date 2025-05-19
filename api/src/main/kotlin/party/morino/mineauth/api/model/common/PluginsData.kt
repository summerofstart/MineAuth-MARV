package party.morino.mineauth.api.model.common

import kotlinx.serialization.Serializable

@Serializable
data class PluginsData(
    val provider : String,
    val plugins : List<String>
)