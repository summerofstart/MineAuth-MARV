package party.morino.moripaapi.web.router.auth.oauth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.data.JWTConfigData
import party.morino.moripaapi.file.utils.KeyUtils.getKeys
import party.morino.moripaapi.web.data.TokenData
import party.morino.moripaapi.web.router.auth.oauth.OAuthRouter.authorizedData
import java.security.MessageDigest
import java.util.*

object TokenRouter: KoinComponent {
    val plugin: MoripaAPI by inject()
    fun Route.tokenRouter() {
        post("/token") {
            val grantType = call.parameters["grant_type"]
            val code = call.parameters["code"]
            val redirectUri = call.parameters["redirect_uri"]
            val clientId = call.parameters["client_id"]
            val codeVerifier = call.parameters["code_verifier"]
            val data = authorizedData[code] ?: run {
                authorizedData.remove(code)
                call.respond(HttpStatusCode.BadRequest, "Invalid code")
                return@post
            }
            plugin.logger.info("Removing $code from authorizedData")
            authorizedData.remove(code)

            if (grantType == null || code == null || redirectUri == null || clientId == null || codeVerifier == null) {
                plugin.logger.warning("Invalid request")
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
                return@post
            }
            if (grantType != "authorization_code") {
                plugin.logger.warning("Invalid grant_type is sent. Expected: authorization_code, Actual: $grantType")
                call.respond(HttpStatusCode.BadRequest, "Invalid grant_type")
                return@post
            }

            if (data.clientId != clientId || data.redirectUri != redirectUri) {
                plugin.logger.warning("Invalid client_id or redirect_uri is sent. Expected: $clientId, $redirectUri, Actual: ${data.clientId}, ${data.redirectUri}")
                call.respond(HttpStatusCode.BadRequest, "Invalid client_id or redirect_uri")
                return@post
            }
            val hash = MessageDigest.getInstance("SHA-256").digest(codeVerifier.toByteArray())
            val base64 = Base64.getUrlEncoder().encodeToString(hash).replace("=", "")
            if (data.codeChallenge != base64) {
                plugin.logger.warning("Invalid code_verifier is sent. Expected: ${data.codeChallenge}, Actual: $base64")
                call.respond(HttpStatusCode.BadRequest, "Invalid code_verifier")
                return@post
            }
            val token = JWT.create().withIssuer(get<JWTConfigData>().issuer).withAudience(data.clientId)
                .withNotBefore(Date(System.currentTimeMillis()))
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .withIssuedAt(Date(System.currentTimeMillis())).withJWTId(UUID.randomUUID().toString())
                .withClaim("client_id", clientId).withClaim("playerUniqueId", data.uniqueId.toString())
                .withClaim("scope", data.scope).withClaim("state", data.state).withClaim("scope", data.scope)
                .withClaim("token_type", "token").sign(
                    Algorithm.RSA256(
                        getKeys().second as java.security.interfaces.RSAPublicKey,
                        getKeys().first as java.security.interfaces.RSAPrivateKey
                    )
                )
            val refreshToken = JWT.create().withIssuer(get<JWTConfigData>().issuer).withAudience(data.clientId)
                .withNotBefore(Date(System.currentTimeMillis()))
                .withExpiresAt(Date(System.currentTimeMillis() + (3_600_000.toLong() * 24 * 30)))
                .withIssuedAt(Date(System.currentTimeMillis())).withJWTId(UUID.randomUUID().toString())
                .withClaim("client_id", clientId).withClaim("playerUniqueId", data.uniqueId.toString())
                .withClaim("scope", data.scope).withClaim("state", data.state).withClaim("scope", data.scope)
                .withClaim("token_type", "refresh_token").sign(
                    Algorithm.RSA256(
                        getKeys().second as java.security.interfaces.RSAPublicKey,
                        getKeys().first as java.security.interfaces.RSAPrivateKey
                    )
                )

            call.respond(
                HttpStatusCode.OK, TokenData(
                    token, "Bearer", 3_600, data.state, refreshToken
                )
            )
            plugin.logger.info("Token issued for ${data.uniqueId}")
        }
    }

}