package party.morino.moripaapi.web.router.auth.oauth

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import party.morino.moripaapi.utils.PlayerUtils.toOfflinePlayer
import party.morino.moripaapi.utils.PlayerUtils.toUUID
import party.morino.moripaapi.web.router.common.data.ProfileData

object ProfileRouter {
    fun Route.profileRouter() {
        authenticate("user-oauth-token") {
            get("/userinfo") {
                val principal = call.principal<JWTPrincipal>()
                val offlinePlayer = principal!!.payload.getClaim("playerUniqueId").asString().toUUID().toOfflinePlayer()
                val username = offlinePlayer.name!!
                val uuid = offlinePlayer.uniqueId
                call.respond(ProfileData(username, uuid))
            }
        }
    }
}
