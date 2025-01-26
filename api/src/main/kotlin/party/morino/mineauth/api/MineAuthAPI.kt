package party.morino.mineauth.api

import org.bukkit.plugin.java.JavaPlugin

interface MineAuthAPI {
    abstract fun createHandler(plugin : JavaPlugin): RegisterHandler
}