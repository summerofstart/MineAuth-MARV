package party.morino.mineauth.addon

import org.bukkit.plugin.java.JavaPlugin
import party.morino.mineauth.addon.routes.ShopRoute
import party.morino.mineauth.api.MineAuthAPI

class QuickShopHikariAddon : JavaPlugin() {

    lateinit var mineAuthAPI: MineAuthAPI

    override fun onEnable() {
        logger.info("QuickShop Hikari Addon enabled")
        mineAuthAPI = server.servicesManager.getRegistration(MineAuthAPI::class.java)?.provider
            ?: throw IllegalStateException("MineAuthAPI not found")

        setupMineauth()
    }

    override fun onDisable() {
        logger.info("QuickShop Hikari Addon disabled")
    }

    private fun setupMineauth() {
        val handler = mineAuthAPI.createHandler(this)

        handler.register(
            ShopRoute()
        )
    }

}