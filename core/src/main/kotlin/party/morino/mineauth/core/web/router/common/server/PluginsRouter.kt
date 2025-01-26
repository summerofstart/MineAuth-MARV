package party.morino.mineauth.core.web.router.common.server

import io.ktor.server.netty.Netty.configuration
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.papermc.paper.command.brigadier.argument.ArgumentTypes.players
import io.papermc.paper.plugin.configuration.PluginMeta
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import party.morino.mineauth.core.web.components.common.ProfileData

object PluginsRouter {
    fun Route.pluginsRoutes() {
        get("/plugins") {
            val plugins = Bukkit.getPluginManager().plugins
            call.respond(plugins.map { it.pluginMeta.getName() })

        }
    }
}