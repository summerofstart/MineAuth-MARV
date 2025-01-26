package party.morino.mineauth.core.integration.quickshop_hikari

import com.ghostchu.quickshop.api.QuickShopAPI
import org.bukkit.Bukkit.getServer
import party.morino.mineauth.core.integration.Integration

object QuickShopIntegration : Integration() {
    override var available: Boolean = false
    override val name: String = "QuickShop-Hikari"
    lateinit var quickshop : QuickShopAPI

    override fun initialize() {
        val plugin = getServer().pluginManager.getPlugin(name)
        plugin?.let {
            mineAuth.logger.info("QuickShop found")
            available = true
            quickshop = QuickShopAPI.getInstance()
        }

    }
}