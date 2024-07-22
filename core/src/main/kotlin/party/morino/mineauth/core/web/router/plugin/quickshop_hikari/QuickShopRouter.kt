package party.morino.mineauth.core.web.router.plugin.quickshop_hikari

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import party.morino.mineauth.core.integration.quickshop_hikari.QuickShopIntegration
import party.morino.mineauth.core.utils.PlayerUtils.toOfflinePlayer
import party.morino.mineauth.core.utils.PlayerUtils.toUUID
import party.morino.mineauth.core.web.JwtCompleteCode

object QuickShopRouter {
    fun Route.quickShopRouter() {
        route("quickshop-hikari"){
            authenticate(JwtCompleteCode.USER_TOKEN.code) {
                get("/shops") {
                    val principal = call.principal<JWTPrincipal>()
                    val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                    val offlinePlayer = uuid.toUUID().toOfflinePlayer()

                    val data = QuickShopIntegration.quickshop.shopManager.allShops.filter {
                        it.owner.uniqueId == offlinePlayer.uniqueId
                    }.map {
                        it.shopId
                    }
                    call.respond(data)
                }
            }
        }

    }
}