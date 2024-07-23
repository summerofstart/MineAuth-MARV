package party.morino.mineauth.core.web.router.auth.oauth

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
import org.koin.core.component.get
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth
import party.morino.mineauth.core.database.UserAuthData
import party.morino.mineauth.core.database.UserAuthData.uuid
import party.morino.mineauth.core.file.data.OAuthConfigData
import party.morino.mineauth.core.utils.json
import party.morino.mineauth.core.web.router.auth.data.AuthorizedData
import party.morino.mineauth.core.web.components.auth.ClientData
import party.morino.mineauth.core.web.router.auth.oauth.OAuthRouter.authorizedData

object AuthorizeRouter: KoinComponent {
    private val plugin: MineAuth by inject()

    fun Route.authorizeRouter() {
        get("/authorize") {
            val responseType = call.parameters["response_type"]
            val clientId = call.parameters["client_id"]
            val redirectUri = call.parameters["redirect_uri"]
            val scope = call.parameters["scope"]
            val state = call.parameters["state"]
            val codeChallenge = call.parameters["code_challenge"]
            val codeChallengeMethod = call.parameters["code_challenge_method"]
            if (clientId == null || redirectUri == null || scope == null || responseType != "code" || state == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@get
            }
            if (codeChallenge == null || codeChallengeMethod != "S256") {
                call.respond(HttpStatusCode.BadRequest, "This server only supports S256 code_challenge_method")
                return@get
            }
            val clientDataFile = plugin.dataFolder.resolve("clients").resolve("$clientId").resolve("data.json")
            if (!clientDataFile.exists()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid client")
                return@get
            }
            val clientData: ClientData =
                json.decodeFromString(clientDataFile.readText()) //TODO make better redirectUri check
            val recordRedirectUri = if(clientData.redirectUri.endsWith("/")) clientData.redirectUri else clientData.redirectUri + "/"
            if (!redirectUri.startsWith(recordRedirectUri)) {
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
                "codeChallenge" to codeChallenge,
                "codeChallengeMethod" to codeChallengeMethod,
                "logoUrl" to get<OAuthConfigData>().logoUrl,
                "applicationName" to get<OAuthConfigData>().applicationName,
            )

            call.respond(VelocityContent("authorize.vm", model))
        }

        post("/authorize") { // authorize method when post from application/x-www-form-urlencoded
            val formParameters = call.receiveParameters()
            val username = formParameters["username"]
            val password = formParameters["password"]
            val responseType = formParameters["response_type"]
            val clientId = formParameters["client_id"]
            val redirectUri = formParameters["redirect_uri"]
            val scope = formParameters["scope"]
            val state = formParameters["state"]
            val codeChallenge = formParameters["code_challenge"]

            if (username == null || password == null || responseType != "code" || clientId == null || redirectUri == null || scope == null || state == null || codeChallenge == null) { //redirectURIにリダイレクト
                val uri = URLBuilder()
                uri.takeFrom(redirectUri!!)
                uri.parameters.apply {
                    append("error", "invalid_request")
                    append("error_description", "It does not have the required parameters")
                    append("state", state!!)
                }
                call.respondRedirect(uri.buildString())
                return@post
            }
            val offlinePlayer = Bukkit.getOfflinePlayer(username)
            if (!offlinePlayer.hasPlayedBefore()) {
                val uri = URLBuilder()
                uri.takeFrom(redirectUri)
                uri.parameters.apply {
                    append("error", "access_denied")
                    append("error_description", "This player has never played before")
                    append("state", state)
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
                uri.takeFrom(redirectUri)
                uri.parameters.apply {
                    append("error", "access_denied")
                    append("error_description", "This player is not registered")
                    append("state", state)
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
                uri.takeFrom(redirectUri)
                uri.parameters.apply {
                    append("error", "access_denied")
                    append("error_description", "Password is incorrect")
                    append("state", state)
                }
                call.respondRedirect(uri.buildString())
                return@post
            } //認証成功
            val code = RandomStringUtils.randomAlphanumeric(16)
            authorizedData[code] = AuthorizedData(clientId, redirectUri, scope, state, codeChallenge, uniqueId)
            val uri = URLBuilder()
            uri.takeFrom(redirectUri)
            uri.parameters.apply {
                append("code", code)
                append("state", state)
            }
            call.respondRedirect(uri.buildString())
            return@post
        }
    }
}