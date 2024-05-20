package party.morino.mineauth.core.web.router.plugin.vault

import io.ktor.server.routing.*

object VaultRouter {
    fun Route.vaultRouter() {
        route("/vault") {
            get {
               this@route
            }
        }
    }
}