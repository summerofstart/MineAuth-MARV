package party.morino.moripaapi.web

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
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
import party.morino.moripaapi.utils.PlayerUtils.toOfflinePlayer
import party.morino.moripaapi.utils.PlayerUtils.toUUID
import party.morino.moripaapi.web.file.config.WebServerConfig
import java.security.KeyStore
import java.util.concurrent.TimeUnit

class WebServer: KoinComponent {
    private val plugin: MoripaAPI by inject()
    private lateinit var originalServer: ApplicationEngine
    fun settingServer() {
        val webServerConfig: WebServerConfig = get()
        plugin.logger.info("Setting up web server")
        val keyStoreFile = plugin.dataFolder.resolve("keystore.jks")
        val keystore = KeyStore.getInstance(keyStoreFile, webServerConfig.ssl.keyStorePassword.toCharArray())
        val environment = applicationEngineEnvironment {
            connector {
                port = webServerConfig.port
            }
            sslConnector(keyStore = keystore,
                         keyAlias = webServerConfig.ssl.keyAlias,
                         keyStorePassword = { webServerConfig.ssl.keyStorePassword.toCharArray() },
                         privateKeyPassword = { webServerConfig.ssl.privateKeyPassword.toCharArray() }) {
                port = webServerConfig.ssl.sslPort
                keyStorePath = keyStoreFile
            }
            module(Application::module)
        }
        originalServer = embeddedServer(Netty, environment)
    }

    fun startServer() {
        originalServer.start(wait = false)
    }

    fun stopServer() {
        originalServer.stop(0, 0, TimeUnit.SECONDS)
    }
}

private fun Application.module() {
    val plugin: MoripaAPI by inject(MoripaAPI::class.java)
    val webServerConfig: WebServerConfig = get(WebServerConfig::class.java)
    install(ContentNegotiation) {
        json()
    }
    val jwkProvider = JwkProviderBuilder(webServerConfig.jwt.issuer).cached(10, 24, TimeUnit.HOURS).rateLimited(
        10, 1, TimeUnit.MINUTES
    ).build()
    install(Authentication) {
        jwt("auth-jwt") {
            realm = webServerConfig.jwt.realm
            verifier(jwkProvider, webServerConfig.jwt.issuer) {
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
        staticFiles(".well-known", plugin.dataFolder, "jwks.json")
    }
}
