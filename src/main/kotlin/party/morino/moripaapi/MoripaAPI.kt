package party.morino.moripaapi

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import org.bukkit.Bukkit
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.dsl.module
import party.morino.moripaapi.commands.HelpCommand
import party.morino.moripaapi.commands.RegisterCommand
import party.morino.moripaapi.database.UserAuthData
import party.morino.moripaapi.file.load.FileUtils
import party.morino.moripaapi.web.WebServer
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.ktx.supportSuspendFunctions
import java.io.File

open class MoripaAPI: SuspendingJavaPlugin() {
    private lateinit var plugin: MoripaAPI
    override suspend fun onEnableAsync() {
        plugin = this
        setCommand()
        setupKoin()
        FileUtils.loadFiles()

        settingDatabase()

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
        }
    }

    private fun settingDatabase() {
        Database.connect(
            url = "jdbc:sqlite:${plugin.dataFolder}${File.separator}Moripa-API.db", driver = "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(UserAuthData)
        }

    }
}