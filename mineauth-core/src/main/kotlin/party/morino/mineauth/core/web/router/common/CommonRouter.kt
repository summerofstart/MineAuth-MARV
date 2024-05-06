package party.morino.mineauth.core.web.router.common

import io.ktor.server.routing.*
import party.morino.mineauth.core.web.router.common.ServerRouter.serverRouter
import party.morino.mineauth.core.web.router.common.UserRouter.userRouter


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