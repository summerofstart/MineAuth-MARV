package party.morino.moripaapi.web.router.auth

import com.password4j.Password
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.velocity.*
import kotlinx.coroutines.Dispatchers
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.database.UserAuthData
import party.morino.moripaapi.database.UserAuthData.uuid
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
                val clientData: ClientData =
                    json.decodeFromString(clientDataFile.readText()) //TODO make better redirectUri check
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

            post("/authorize") { // 認証処理 formから送られてきたデータを取得して認証処理を行う
                val formParameters = call.receiveParameters()
                val username = formParameters["username"]
                val password = formParameters["password"]
                val responseType = formParameters["response_type"]
                val clientId = formParameters["client_id"]
                val redirectURI = formParameters["redirect_uri"]
                val scope = formParameters["scope"]
                val state = formParameters["state"]

                if (username == null || password == null || responseType != "code" || clientId == null || redirectURI == null || scope == null || state == null) { //redirectURIにリダイレクト
                    val uri = URLBuilder()
                    uri.takeFrom(redirectURI!!)
                    uri.parameters.apply {
                        append("error", "invalid_request")
                        append("error_description", "It does not have the required parameters")
                    }
                    call.respondRedirect(uri.buildString())
                    return@post
                }
                val offlinePlayer = Bukkit.getOfflinePlayer(username)
                if (!offlinePlayer.hasPlayedBefore()) { //401
                    val uri = URLBuilder()
                    uri.takeFrom(redirectURI)
                    uri.parameters.apply {
                        append("error", "access_denied")
                        append("error_description", "This player has never played before")
                    }
                    call.respondRedirect(uri.buildString())
                    return@post
                }
                val uniqueId = offlinePlayer.uniqueId
                val exist = newSuspendedTransaction(Dispatchers.IO) {
                    UserAuthData.selectAll().where { uuid eq uniqueId.toString() }.count() > 0
                }
                if (!exist) { //401
                    val uri = URLBuilder()
                    uri.takeFrom(redirectURI)
                    uri.parameters.apply {
                        append("error", "access_denied")
                        append("error_description", "This player is not registered")
                    }
                    call.respondRedirect(uri.buildString())
                    return@post
                }
                val hashedPassword = newSuspendedTransaction {
                    UserAuthData.selectAll().where { uuid eq uniqueId.toString() }.first()[UserAuthData.password]
                }
                val check = Password.check(password, hashedPassword).addPepper().withArgon2()
                if (!check) { //401
                    val uri = URLBuilder()
                    uri.takeFrom(redirectURI)
                    uri.parameters.apply {
                        append("error", "access_denied")
                        append("error_description", "Password is incorrect")
                    }
                    call.respondRedirect(uri.buildString())
                    return@post
                } //認証成功
                val code = RandomStringUtils.randomAlphanumeric(16)
                val uri = URLBuilder()
                uri.takeFrom(redirectURI)
                uri.parameters.apply {
                    append("code", code)
                    append("state", state)
                }
                call.respondRedirect(uri.buildString())
                return@post
            }
        }
    }
}