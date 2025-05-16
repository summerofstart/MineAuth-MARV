package party.morino.mineauth.addons.quickshop.routes

import com.ghostchu.quickshop.api.QuickShopAPI
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.addon.data.ShopData
import party.morino.mineauth.api.annotations.AuthedAccessUser
import party.morino.mineauth.api.annotations.GetMapping
import party.morino.mineauth.api.annotations.Permission

// plugin名がmineauth-api-quickshop-hikari-addonなので plugins/mineauth-api-quickshop-hikari-addon/shopsにアクセスするとここに飛ぶ
class ShopRoute : KoinComponent {
    val quickShopAPI: QuickShopAPI by inject()

    @GetMapping("/id/:id")
    @Permission("quickshop-hikari.shop")
    fun user(@AuthedAccessUser player: Player, id: String): ShopData? {

        val shops = quickShopAPI.shopManager.allShops.filter {
            it.shopId.toString()==id
        }

        if (shops.isEmpty()) {
            return null
        }

        val shop = shops.first()

        val data = ShopData(shop.shopId, shop.location.toString())

        return data
    }
}