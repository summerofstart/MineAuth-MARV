package party.morino.mineauth.core.web.router.common.server

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.bukkit.Bukkit

object PluginsRouter {
    fun Route.pluginsRoutes() {
        get("/plugins") {
            val plugins = Bukkit.getPluginManager().plugins
            call.respond(plugins.map { it.pluginMeta.getName() })

        }
    }
}