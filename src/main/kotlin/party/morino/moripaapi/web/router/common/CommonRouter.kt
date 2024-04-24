package party.morino.moripaapi.web.router.common

import io.ktor.server.routing.*
import party.morino.moripaapi.web.router.common.ServerRouter.serverRouter
import party.morino.moripaapi.web.router.common.UserRouter.userRouter


object CommonRouter {
    fun Route.commonRouter() {
        route("/server") {
            serverRouter()
        }
        route("/user") {
            userRouter()
        }
    }
}