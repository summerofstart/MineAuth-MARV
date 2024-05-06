package party.morino.mineauth.core.utils

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.*

object PlayerUtils {
    fun String.toUUID(): UUID {
        return UUID.fromString(this)
    }

    fun UUID.toOfflinePlayer(): OfflinePlayer {
        return Bukkit.getOfflinePlayer(this)
    }

}