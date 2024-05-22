package party.morino.mineauth.core.web.router.plugin

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import party.morino.mineauth.core.integration.IntegrationInitializer
import party.morino.mineauth.core.integration.vault.VaultIntegration
import party.morino.mineauth.core.web.router.plugin.vault.VaultRouter.vaultRouter

object PluginRouter {
    fun Route.pluginRouter() {
        get {
            call.respondText("Hello, plugin!")
        }
        get("/availableIntegrators") {
            call.respond(IntegrationInitializer.availableIntegrations.map { it.name })
        }
        if (VaultIntegration.available) {
            vaultRouter()
        }
    }
}