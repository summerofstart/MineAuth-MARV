package party.morino.mineauth.core.web.router.common

import io.ktor.server.routing.*
import party.morino.mineauth.core.web.router.common.server.PlayersRouter.playersRouter

object ServerRouter {
    fun Route.serverRouter() {
        playersRouter()
    }
}