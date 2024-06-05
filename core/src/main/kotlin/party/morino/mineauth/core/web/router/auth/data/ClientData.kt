package party.morino.mineauth.core.web.router.auth.data

import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth
import party.morino.mineauth.core.utils.json

@Serializable
sealed class ClientData: KoinComponent {
    abstract val clientId: String
    abstract val clientName: String
    abstract val redirectUri: String

    @Serializable
    data class PublicClientData(
        override val clientId: String, override val clientName: String, override val redirectUri: String
    ): ClientData()

    @Serializable
    data class ConfidentialClientData(
        override val clientId: String, override val clientName: String, override val redirectUri: String, val clientSecret: String
    ): ClientData()

    companion object : KoinComponent{
        private val plugin by inject<MineAuth>()
        fun getClientData(clientId: String): ClientData {
            val file = plugin.dataFolder.resolve("clients").resolve(clientId).resolve("data.json")
            return json.decodeFromString(file.readText())
        }
    }
}


