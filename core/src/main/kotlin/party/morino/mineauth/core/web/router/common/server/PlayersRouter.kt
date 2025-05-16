package party.morino.mineauth.core.web.router.common.server

import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.bukkit.Bukkit
import party.morino.mineauth.api.model.common.ProfileData

object PlayersRouter {
    fun Route.playersRouter() {
        get("/players") {
            val players = Bukkit.getOnlinePlayers().map { ProfileData(it.name, it.uniqueId) }
            call.respond(players)
        }
    }
}