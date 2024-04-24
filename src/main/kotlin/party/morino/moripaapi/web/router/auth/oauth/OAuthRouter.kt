package party.morino.moripaapi.web.router.auth.oauth

import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import party.morino.moripaapi.web.data.AuthorizedData
import party.morino.moripaapi.web.router.auth.oauth.AuthorizeRouter.authorizeRouter
import party.morino.moripaapi.web.router.auth.oauth.ProfileRouter.profileRouter
import party.morino.moripaapi.web.router.auth.oauth.TokenRouter.tokenRouter

object OAuthRouter: KoinComponent {
    fun Route.oauthRouter() {
        route("/oauth2") {
            authorizeRouter()
            tokenRouter()
            profileRouter()
        }
    }

    val authorizedData = hashMapOf<String, AuthorizedData>()
}