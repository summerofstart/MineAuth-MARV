package party.morino.moripaapi.web.router.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.velocity.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.data.OAuthConfigData
import party.morino.moripaapi.utils.json
import party.morino.moripaapi.web.data.ClientData

object OAuthRouter: KoinComponent {
    private val plugin: MoripaAPI by inject()
    private val OAuthConfigData: OAuthConfigData by inject()
    fun Route.oauthRouter() {
        route("/oauth2") {
            get("/authorize") {
                val responseType = call.parameters["response_type"]
                val clientId = call.parameters["client_id"]
                val redirectUri = call.parameters["redirect_uri"]
                val scope = call.parameters["scope"]
                val state = call.parameters["state"]

                if (clientId == null || redirectUri == null || scope == null || responseType != "code" || state == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid request")
                    return@get
                }

                val clientDataFile = plugin.dataFolder.resolve("clients").resolve("$clientId").resolve("data.json")
                if (!clientDataFile.exists()) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid client")
                    return@get
                }
                val clientData: ClientData = json.decodeFromString(clientDataFile.readText()) //TODO make better redirectUri check
                if ((redirectUri.startsWith("http://") || redirectUri.startsWith("http://")) && redirectUri.startsWith(
                        clientData.redirectUri
                    )
                ) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid redirect_uri")
                    return@get
                } // 認証画面を返す
                val model = mapOf(
                    "clientId" to clientData.clientId,
                    "clientName" to clientData.clientName,
                    "redirectUri" to redirectUri,
                    "responseType" to "code",
                    "state" to state,
                    "scope" to scope,

                    "logoUrl" to OAuthConfigData.logoUrl,
                    "applicationName" to OAuthConfigData.applicationName,
                )

                call.respond(VelocityContent("authorize.vm", model))
            }

            post {}
        }
    }
}