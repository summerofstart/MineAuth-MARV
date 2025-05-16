package party.morino.mineauth.addons.quickshop.data

import kotlinx.serialization.Serializable

@Serializable
data class ShopSetting(
    val price: Double,
    val mode: ShopMode,
    val perBulkAmount: Int
)