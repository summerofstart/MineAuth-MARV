package party.morino.mineauth.addons.quickshop

import com.ghostchu.quickshop.api.QuickShopAPI
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.addons.quickshop.data.
import party.morino.mineauth.api.annotations.Authenticated
import party.morino.mineauth.api.annotations.HttpHandler
import party.morino.mineauth.api.http.HttpMethod
import party.morino.mineauth.api.http.HttpResponse
import party.morino.mineauth.api.http.HttpStatus

class ShopHandler: KoinComponent {
    val quickshopAPI: QuickShopAPI by inject()

    @HttpHandler(path = "/shop/items/<id>", method = HttpMethod.GET)
    suspend fun getShopItems(@Authenticated player: Player, @PathVariable id: Long): HttpResponse {
        // プレイヤーに関連するショップアイテムを取得

        val shop = quickshopAPI.shopManager.getShop(id) ?: return HttpResponse(status = HttpStatus.NOT_FOUND.code, "Shop not found")
        val owner = shop.owner.uniqueId
        if (owner != player.uniqueId) {
            return HttpResponse(status = HttpStatus.FORBIDDEN.code, "You don't have permission to use this shop")
        }

        val items = ShopData(shop.shopId, shop.location)
        return HttpResponse(
            status = HttpStatus.OK.code,
            body = items.toJson()
        )
    }

    private fun getItemsForPlayer(player: Player): List<Item> {
        // アイテム取得のロジックを実装
        return listOf()
    }
} 