package party.morino.moripaapi

import party.morino.moripaapi.commands.HelpCommand
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions

open class MoripaAPI : JavaPlugin() {
    private lateinit var plugin: MoripaAPI
    override fun onEnable() {
        // Plugin startup logic
        plugin = this
        setCommand()
        setupKoin()
    }

    private fun setupKoin() {
        val appModule = module {
            single<MoripaAPI> { this@MoripaAPI }
        }

        GlobalContext.getOrNull() ?: GlobalContext.startKoin {
            modules(appModule)
        }
    }
    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun setCommand() {
        val handler = BukkitCommandHandler.create(this)

        handler.setSwitchPrefix("--")
        handler.setFlagPrefix("--")
        handler.supportSuspendFunctions()

        handler.setHelpWriter { command, actor ->
            java.lang.String.format(
                """
                <color:yellow>command: <color:gray>%s
                <color:yellow>usage: <color:gray>%s
                <color:yellow>description: <color:gray>%s
                
                """.trimIndent(),
                command.path.toList(),
                command.usage,
                command.description,
            )
        }

        with(handler) {
            register(HelpCommand())
        }
    }
}