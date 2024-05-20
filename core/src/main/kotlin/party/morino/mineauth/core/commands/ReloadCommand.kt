package party.morino.mineauth.core.commands

import org.bukkit.command.CommandSender
import party.morino.mineauth.core.file.load.FileUtils
import party.morino.mineauth.core.web.WebServer
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand
import revxrsal.commands.bukkit.annotation.CommandPermission

@Command("mineauth", "ma", "mauth")
class ReloadCommand {
    @Subcommand("reload")
    @CommandPermission("moripaapi.command.reload")
    fun reload(sender: CommandSender) {
        sender.sendMessage("Reloading...")

        WebServer.stopServer()
        sender.sendMessage("Server stopped")

        FileUtils.loadFiles()
        sender.sendMessage("Files reloaded")

        FileUtils.settingDatabase()

        WebServer.settingServer()
        WebServer.startServer()
        sender.sendMessage("Server started")

        sender.sendMessage("Reloaded")
    }

}