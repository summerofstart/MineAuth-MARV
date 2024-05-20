package party.morino.mineauth.core.file.data

import kotlinx.serialization.Serializable
import party.morino.mineauth.core.utils.UUIDSerializer
import java.util.*

@Serializable
data class JWTConfigData(
    val issuer: String = "https://api.morino.party",
    val audience: String = "https://dash.morino.party",
    val realm: String = "morino.party",
    val privateKeyFile: String = "privateKey.pem",
    val keyId: @Serializable(with = UUIDSerializer::class) UUID
)