package party.morino.mineauth.core.integration.quickshop_hikari

import org.bukkit.Bukkit.getServer
import party.morino.mineauth.core.integration.Integration
import com.ghostchu.quickshop.api.QuickShopAPI

object QuickshopIntegration : Integration {
    override var available: Boolean = false
    override val name: String = "QuickShop-Hikari"
    lateinit var quickshop : QuickShopAPI

    override fun initialize() {
        val plugin = getServer().pluginManager.getPlugin(name)
        plugin?.let {
            available = true
        }
        quickshop = QuickShopAPI.getInstance()
    }
}