package party.morino.moripaapi.web.router.auth

import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI

object WellKnownRouter: KoinComponent {
    val plugin: MoripaAPI by inject()
    fun Route.wellKnownRouter() {
        staticFiles(".well-known", plugin.dataFolder, "jwks.json")
    }
}