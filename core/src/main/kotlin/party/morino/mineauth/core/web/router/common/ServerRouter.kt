package party.morino.mineauth.core.web.router.common

import io.ktor.server.routing.*
import party.morino.mineauth.core.web.router.common.server.PlayersRouter.playersRouter
import party.morino.mineauth.core.web.router.common.server.PluginsRouter.pluginsRoutes

object ServerRouter {
    fun Route.serverRouter() {
        playersRouter()
        pluginsRoutes()
    }
}