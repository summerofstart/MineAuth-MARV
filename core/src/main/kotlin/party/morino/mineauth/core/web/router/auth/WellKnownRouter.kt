package party.morino.mineauth.core.web.router.auth

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth

object WellKnownRouter: KoinComponent {
    private val plugin: MineAuth by inject()
    fun Route.wellKnownRouter() {
        staticFiles(".well-known", plugin.dataFolder, "jwks.json")
    }
}