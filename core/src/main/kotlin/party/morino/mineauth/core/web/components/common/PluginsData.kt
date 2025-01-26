package party.morino.mineauth.core.web.components.common

import kotlinx.serialization.Serializable

@Serializable
data class PluginsData(
    val provider : String,
    val plugins : List<String>
)
