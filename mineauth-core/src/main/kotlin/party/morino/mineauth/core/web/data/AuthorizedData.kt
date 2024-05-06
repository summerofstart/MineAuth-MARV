package party.morino.mineauth.core.web.data

import kotlinx.serialization.Serializable
import party.morino.mineauth.core.utils.UUIDSerializer
import java.util.*

@Serializable
data class AuthorizedData(
    val clientId: String,
    val redirectUri: String,
    val scope: String,
    val state: String,
    val codeChallenge: String,
    val uniqueId: @Serializable(with = UUIDSerializer::class) UUID
)
