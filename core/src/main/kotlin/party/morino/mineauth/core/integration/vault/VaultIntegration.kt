package party.morino.mineauth.core.integration.vault

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit.getServer
import party.morino.mineauth.core.integration.Integration


object VaultIntegration : Integration {
    override var available: Boolean = false
    override val name: String = "Vault"
    lateinit var economy : Economy

    override fun initialize() {
        val plugin = getServer().pluginManager.getPlugin(name)
        plugin?.let {
            available = true
        }
        val rsp = getServer().servicesManager.getRegistration(
            Economy::class.java
        )
        if (rsp == null) {
            available = false
            return
        }
        economy = rsp.provider
    }

}