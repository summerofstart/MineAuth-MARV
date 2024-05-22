package party.morino.mineauth.core.web.router.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class ClientData(
    val clientId: String, val clientName: String, val redirectUri: String
)
