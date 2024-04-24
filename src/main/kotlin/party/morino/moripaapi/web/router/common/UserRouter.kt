package party.morino.moripaapi.web.router.common

import io.ktor.server.routing.*
import party.morino.moripaapi.web.router.common.user.ProfileRouter.profileRouter

object UserRouter {
    fun Route.userRouter() {
        profileRouter()
    }
}