package party.morino.moripaapi.web.file.config

import kotlinx.serialization.Serializable

@Serializable
data class WebServerConfig(
    val port: Int,
    val jwt: JWTConfig,
    val ssl: SSLConfig
)
@Serializable
data class SSLConfig(
    val sslPort: Int,
    val keyStore: String,
    val keyAlias: String,
    val keyStorePassword: String,
    val privateKeyPassword: String
)

@Serializable
data class JWTConfig(
    val issuer: String,
    val audience: String,
    val realm: String,
    val privateKey : String,
    val keyId : String
)
