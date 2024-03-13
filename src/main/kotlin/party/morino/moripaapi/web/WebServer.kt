package party.morino.moripaapi.web

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.get
import org.koin.java.KoinJavaComponent.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.config.JWTConfigData
import party.morino.moripaapi.file.config.WebServerConfigData
import party.morino.moripaapi.utils.PlayerUtils.toOfflinePlayer
import party.morino.moripaapi.utils.PlayerUtils.toUUID
import party.morino.moripaapi.web.router.auth.oauth.OAuthRouter.oauthRouter
import java.security.KeyStore
import java.util.concurrent.TimeUnit

object WebServer: KoinComponent {
    private val plugin: MoripaAPI by inject()
    private var originalServer: ApplicationEngine? = null
    fun settingServer() {
        val webServerConfigData: WebServerConfigData = get()
        plugin.logger.info("Setting up web server")
        val environment = applicationEngineEnvironment {
            connector {
                port = webServerConfigData.port
            }
            if (webServerConfigData.ssl != null) {
                val keyStoreFile = plugin.dataFolder.resolve("keystore.jks")
                val keystore =
                    KeyStore.getInstance(keyStoreFile, webServerConfigData.ssl.keyStorePassword.toCharArray())
                sslConnector(keyStore = keystore,
                             keyAlias = webServerConfigData.ssl.keyAlias,
                             keyStorePassword = { webServerConfigData.ssl.keyStorePassword.toCharArray() },
                             privateKeyPassword = { webServerConfigData.ssl.privateKeyPassword.toCharArray() }) {
                    port = webServerConfigData.ssl.sslPort
                    keyStorePath = keyStoreFile
                }
            }
            module(Application::module)
        }
        originalServer = embeddedServer(Netty, environment)
    }

    fun startServer() {
        originalServer?.start(wait = false)
    }

    fun stopServer() {
        originalServer?.stop(0, 0, TimeUnit.SECONDS)
    }
}

private fun Application.module() {
    val plugin: MoripaAPI by inject(MoripaAPI::class.java)
    val webServerConfigData: WebServerConfigData = get(WebServerConfigData::class.java)
    val jwtConfigData: JWTConfigData = get(JWTConfigData::class.java)
    install(ContentNegotiation) {
        json()
    }
    val jwkProvider = JwkProviderBuilder(jwtConfigData.issuer).cached(10, 24, TimeUnit.HOURS).rateLimited(
        10, 1, TimeUnit.MINUTES
    ).build()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = jwtConfigData.realm
            verifier(jwkProvider, jwtConfigData.issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                if (credential.payload.getClaim("username").asString() == credential.payload.getClaim("uuid").asString()
                        .toUUID().toOfflinePlayer().name
                ) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        route("/") {
            get {
                call.respondText("Hello World!")
            }
        }
        oauthRouter()

        authenticate("auth-jwt") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val uuid = principal!!.payload.getClaim("uuid").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText(
                    "Hello, ${
                        uuid.toUUID().toOfflinePlayer().name
                    }! Token is expired at $expiresAt ms."
                )
            }
        }
    }
}
