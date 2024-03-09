package party.morino.moripaapi.file.config

import kotlinx.serialization.Serializable
import party.morino.moripaapi.utils.UUIDSerializer
import java.util.*

@Serializable
data class WebServerConfigData(
    val port: Int = 8080, val ssl: SSLConfigData?
)

@Serializable
data class SSLConfigData(
    val sslPort: Int,
    val keyStore: String,
    val keyAlias: String,
    val keyStorePassword: String,
    val privateKeyPassword: String
)

@Serializable
data class JWTConfigData(
    val issuer: String = "https://api.morino.party",
    val audience: String = "https://dash.morino.party",
    val realm: String = "morino.party",
    val privateKeyFile: String = "privateKey.pem",
    val keyId: @Serializable(with = UUIDSerializer::class) UUID
)
