package party.morino.mineauth.api.model.common

@Serializable
data class PluginsData(
    val provider : String,
    val plugins : List<String>
)