package party.morino.moripaapi.web.router.auth

import io.ktor.server.routing.*
import party.morino.moripaapi.web.router.auth.LoginRouter.loginRouter
import party.morino.moripaapi.web.router.auth.OAuthRouter.oauthRouter
import party.morino.moripaapi.web.router.auth.WellKnownRouter.wellKnownRouter

object AuthRouter {
    fun Route.authRouter() {
        wellKnownRouter()
        oauthRouter()
        loginRouter()
    }
}