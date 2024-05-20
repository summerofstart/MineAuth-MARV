package party.morino.mineauth.core.web.data

import kotlinx.serialization.Serializable

@Serializable
data class ClientData(
    val clientId: String, val clientName: String, val redirectUri: String
)
