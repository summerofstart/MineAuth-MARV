package party.morino.mineauth.core.file.data

import kotlinx.serialization.Serializable

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


