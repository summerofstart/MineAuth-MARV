package party.morino.mineauth.core.web.router.plugin

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

object PluginRouter {
    fun Route.pluginRouter() {
        route("/plugin") {
            get {
                call.respondText("Hello, plugin!")
            }
        }
    }
}