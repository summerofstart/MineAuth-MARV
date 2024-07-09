package party.morino.mineauth.core.web.components.plugin.quickshop_hikari

import kotlinx.serialization.Serializable
import party.morino.mineauth.core.utils.UUIDSerializer
import party.morino.mineauth.core.web.components.bukkit.ItemStackData
import party.morino.mineauth.core.web.components.bukkit.LocationData
import java.util.*

@Serializable
data class ShopData(
    val shopId:@Serializable(with = UUIDSerializer::class) UUID,
    val owner: String,
    val location : LocationData,
    val price: Double,
    val item: ItemStackData,
)