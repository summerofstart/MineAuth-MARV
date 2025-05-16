package party.morino.mineauth.core.web.router.auth.oauth

import com.password4j.Password
import io.ktor.http.*
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
import party.morino.mineauth.core.web.components.auth.ClientData
import party.morino.mineauth.core.web.router.auth.data.AuthorizedData
import party.morino.mineauth.core.web.router.auth.oauth.OAuthRouter.authorizedData

/**
 * OAuth2.0の認可エンドポイントを提供するルーター
 * 認可コードフローを実装しています
 */
object AuthorizeRouter: KoinComponent {
    private val plugin: MineAuth by inject()

    /**
     * 認可エンドポイントのルーティングを設定
     * GET /authorize: 認可画面を表示
     * POST /authorize: 認可リクエストを処理
     */
    fun Route.authorizeRouter() {
        // 認可画面を表示するエンドポイント
        get("/authorize") {
            // OAuth2.0の必須パラメータを取得
            val responseType = call.parameters["response_type"]
            val clientId = call.parameters["client_id"]
            val redirectUri = call.parameters["redirect_uri"]
            val scope = call.parameters["scope"]
            val state = call.parameters["state"]
            val codeChallenge = call.parameters["code_challenge"]
            val codeChallengeMethod = call.parameters["code_challenge_method"]

            // 必須パラメータのバリデーション
            if (clientId == null || redirectUri == null || scope == null || responseType != "code" || state == null) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@get
            }

            // PKCE(Proof Key for Code Exchange)のバリデーション
            // S256メソッドのみをサポート
            if (codeChallenge == null || codeChallengeMethod != "S256") {
                call.respond(HttpStatusCode.BadRequest, "This server only supports S256 code_challenge_method")
                return@get
            }

            // クライアントの存在確認
            val clientDataFile = plugin.dataFolder.resolve("clients").resolve("$clientId").resolve("data.json")
            if (!clientDataFile.exists()) {
                call.respond(HttpStatusCode.BadRequest, "Invalid client")
                return@get
            }

            // クライアント情報の読み込みとリダイレクトURIの検証
            val clientData: ClientData = json.decodeFromString(clientDataFile.readText())
            val recordRedirectUri = if(clientData.redirectUri.endsWith("/")) clientData.redirectUri else clientData.redirectUri + "/"
            if (!redirectUri.startsWith(recordRedirectUri)) {
                call.respond(HttpStatusCode.BadRequest, "Invalid redirect_uri")
                return@get
            }

            // 認可画面に表示するデータの準備
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

            // Velocityテンプレートを使用して認可画面を表示
            call.respond(VelocityContent("authorize.vm", model))
        }

        // 認可リクエストを処理するエンドポイント
        post("/authorize") {
            // フォームパラメータの取得
            val formParameters = call.receiveParameters()
            val username = formParameters["username"]
            val password = formParameters["password"]
            val responseType = formParameters["response_type"]
            val clientId = formParameters["client_id"]
            val redirectUri = formParameters["redirect_uri"]
            val scope = formParameters["scope"]
            val state = formParameters["state"]
            val codeChallenge = formParameters["code_challenge"]

            // パラメータのバリデーション
            if (username == null || password == null || responseType != "code" || clientId == null || redirectUri == null || scope == null || state == null || codeChallenge == null) {
                // エラーパラメータを付加してリダイレクト
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

            // プレイヤーの存在確認
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

            // ユーザー認証データの存在確認
            val uniqueId = offlinePlayer.uniqueId
            val exist = newSuspendedTransaction(Dispatchers.IO) {
                UserAuthData.selectAll().where { uuid eq uniqueId.toString() }.count() > 0
            }
            if (!exist) {
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

            // パスワードの検証
            val hashedPassword = newSuspendedTransaction {
                UserAuthData.selectAll().where { uuid eq uniqueId.toString() }.first()[UserAuthData.password]
            }
            val check = Password.check(password, hashedPassword).addPepper().withArgon2()
            if (!check) {
                val uri = URLBuilder()
                uri.takeFrom(redirectUri)
                uri.parameters.apply {
                    append("error", "access_denied")
                    append("error_description", "Password is incorrect")
                    append("state", state)
                }
                call.respondRedirect(uri.buildString())
                return@post
            }

            // 認可コードの生成と保存
            val code = RandomStringUtils.randomAlphanumeric(16)
            authorizedData[code] = AuthorizedData(clientId, redirectUri, scope, state, codeChallenge, uniqueId)

            // 認可コードをリダイレクトURIに付加してリダイレクト
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