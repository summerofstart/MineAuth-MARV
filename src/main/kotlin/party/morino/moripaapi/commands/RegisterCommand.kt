package party.morino.moripaapi.commands

import com.password4j.Password
import kotlinx.coroutines.Dispatchers
import org.apache.commons.lang3.RandomStringUtils
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.update
import party.morino.moripaapi.database.UserAuthData
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Subcommand

@Command("moripaapi", "ma", "mapi")
class RegisterCommand {
    @Subcommand("register")
    suspend fun register(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return
        }
        sender.sendMessage("Registering...")
        val exist = newSuspendedTransaction(Dispatchers.IO) {
            UserAuthData.selectAll().where { UserAuthData.uuid eq sender.uniqueId.toString() }.count()
        } > 0
        if (exist) {
            sender.sendMessage("You are already registered if you want to change your password use /moripaapi change")
            return
        }
        val password = RandomStringUtils.randomAlphanumeric(20)
        sender.sendRichMessage(
            "Password is  $password. <yellow><click:copy_to_clipboard:'$password'>Click to copy</click></yellow>"
        )
        val hashed = Password.hash(password).addRandomSalt().addPepper().withArgon2().result

        newSuspendedTransaction(Dispatchers.IO) {
            UserAuthData.insert {
                it[uuid] = sender.uniqueId.toString()
                it[UserAuthData.password] = hashed
            }
        }
    }

    @Subcommand("change")
    suspend fun change(sender: CommandSender) {
        if (sender !is Player) {
            sender.sendMessage("You must be a player to use this command")
            return
        }
        sender.sendMessage("Changing...")
        val exist = newSuspendedTransaction(Dispatchers.IO) {
            UserAuthData.selectAll().where { UserAuthData.uuid eq sender.uniqueId.toString() }.count()
        } > 0
        if (!exist) {
            sender.sendMessage("You are not already registered if you want to register use /moripaapi register")
            return
        }
        val password = RandomStringUtils.randomAlphanumeric(20)
        sender.sendRichMessage(
            "Password is  $password. <yellow><click:copy_to_clipboard:'$password'>Click to copy</click></yellow>"
        )
        val hashed = Password.hash(password).addRandomSalt().addPepper().withArgon2().result
        newSuspendedTransaction(Dispatchers.IO) {
            UserAuthData.update({ UserAuthData.uuid eq sender.uniqueId.toString() }) {
                it[UserAuthData.password] = hashed
            }
        }
    }
}