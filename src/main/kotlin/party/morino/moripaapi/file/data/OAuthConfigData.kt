package party.morino.moripaapi.file.data

import kotlinx.serialization.Serializable

@Serializable
data class OAuthConfigData(
    val applicationName: String,
    val logoUrl: String,
)