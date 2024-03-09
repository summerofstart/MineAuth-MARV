package party.morino.moripaapi

import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import party.morino.moripaapi.commands.HelpCommand
import party.morino.moripaapi.file.Config
import party.morino.moripaapi.utils.coroutines.async
import party.morino.moripaapi.web.WebServer
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

open class MoripaAPI: JavaPlugin() {
    private lateinit var plugin: MoripaAPI
    override fun onEnable() { // Plugin startup logic
        plugin = this
        setCommand()
        setupKoin()
        Config.loadConfig()

        Bukkit.getScheduler().runTask(plugin, Runnable {
            WebServer.settingServer()
            WebServer.startServer()
        })

    }

    private fun setupKoin() {
        val appModule = module {
            single<MoripaAPI> { this@MoripaAPI }
        }

        GlobalContext.getOrNull() ?: GlobalContext.startKoin {
            modules(appModule)
        }
    }

    override fun onDisable() { // Plugin shutdown logic
        WebServer.stopServer()
    }

    private fun setCommand() {
        val handler = BukkitCommandHandler.create(this)

        handler.setSwitchPrefix("--")
        handler.setFlagPrefix("--")
        handler.supportSuspendFunctions()

        handler.setHelpWriter { command, _ ->
            java.lang.String.format(
                """
                <color:yellow>command: <color:gray>%s %s
                <color:yellow>description: <color:gray>%s
                
                """.trimIndent(), command.path.toRealString(), command.usage, command.description
            )
        }

        with(handler) {
            register(HelpCommand())
        }
    }
}