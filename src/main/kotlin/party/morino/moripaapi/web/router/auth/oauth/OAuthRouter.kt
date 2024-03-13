package party.morino.moripaapi.web.router.auth.oauth

import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI

object OAuthRouter: KoinComponent {
    val plugin: MoripaAPI by inject()
    fun Route.oauthRouter() {
        route("/") {}
    }
}