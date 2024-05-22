package party.morino.mineauth.core.web.router.plugin.vault.data

import kotlinx.serialization.Serializable
import org.bukkit.OfflinePlayer
import party.morino.mineauth.core.utils.OfflinePlayerSerializer

@Serializable
data class RemittanceData(
    val target : @Serializable(with = OfflinePlayerSerializer::class) OfflinePlayer,
    val amount : Double
)
