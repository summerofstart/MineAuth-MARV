package party.morino.mineauth.core

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import party.morino.mineauth.api.MineAuthAPI
import party.morino.mineauth.api.RegisterHandler
import party.morino.mineauth.core.commands.HelpCommand
import party.morino.mineauth.core.commands.RegisterCommand
import party.morino.mineauth.core.commands.ReloadCommand
import party.morino.mineauth.core.file.load.FileUtils
import party.morino.mineauth.core.integration.IntegrationInitializer
import party.morino.mineauth.core.web.WebServer
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

open class MineAuth: SuspendingJavaPlugin() , MineAuthAPI {
    private lateinit var plugin: MineAuth
    override suspend fun onEnableAsync() {
        plugin = this
        setCommand()
        setupKoin()
        FileUtils.loadFiles()
        FileUtils.settingDatabase()
        IntegrationInitializer.initialize()
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            WebServer.settingServer()
            WebServer.startServer()
        })
    }


    private fun setupKoin() {
        val appModule = module {
            single<MineAuth> { this@MineAuth }
        }

        GlobalContext.getOrNull() ?: GlobalContext.startKoin {
            modules(appModule)
        }
    }

    override suspend fun onDisableAsync() {
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
            register(RegisterCommand())
            register(ReloadCommand())
        }
    }

    override fun createHandler(plugin: JavaPlugin): RegisterHandler {
        TODO("Not yet implemented")
    }

}