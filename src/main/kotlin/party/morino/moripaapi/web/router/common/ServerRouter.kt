package party.morino.moripaapi.web.router.common

import io.ktor.server.routing.*
import party.morino.moripaapi.web.router.common.server.PlayersRouter.playersRouter

object ServerRouter {
    fun Route.serverRouter() {
        playersRouter()
    }
}