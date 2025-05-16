package party.morino.mineauth.core.web.router.plugin.quickshop_hikari

import com.ghostchu.quickshop.api.shop.Shop
import com.ghostchu.quickshop.api.shop.ShopType
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.Material
import party.morino.mineauth.core.integration.quickshop_hikari.QuickShopIntegration
import party.morino.mineauth.core.utils.PlayerUtils.toOfflinePlayer
import party.morino.mineauth.core.utils.PlayerUtils.toUUID
import party.morino.mineauth.core.utils.coroutines.minecraft
import party.morino.mineauth.core.web.JwtCompleteCode
import party.morino.mineauth.api.model.bukkit.ItemStackData
import party.morino.mineauth.api.model.bukkit.LocationData
import party.morino.mineauth.core.web.components.plugin.quickshop_hikari.ShopData
import party.morino.mineauth.core.web.components.plugin.quickshop_hikari.ShopMode
import party.morino.mineauth.core.web.components.plugin.quickshop_hikari.ShopSetting

object QuickShopRouter {
    fun Route.quickShopRouter() {
        route("quickshop-hikari") {
            authenticate(JwtCompleteCode.USER_TOKEN.code) {
                get("/users/me/shops") {
                    val principal = call.principal<JWTPrincipal>()
                    val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                    val offlinePlayer = uuid.toUUID().toOfflinePlayer()

                    val data = QuickShopIntegration.quickshop.shopManager.allShops.filter {
                        it.owner.uniqueId == offlinePlayer.uniqueId
                    }.map {
                        it.shopId
                    }
                    call.respond(data)
                }

                route("/shops/{shopId}/setting") {
                    get {
                        val shopId = call.parameters["shopId"]?.toLongOrNull()
                            ?: return@get call.respond("Invalid shop id")
                        val shop = QuickShopIntegration.quickshop.shopManager.getShop(shopId)
                            ?: return@get call.respond("Shop not found")

                        val principal = call.principal<JWTPrincipal>()
                        val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                        val offlinePlayer = uuid.toUUID().toOfflinePlayer()

                        if (shop.owner.uniqueId != offlinePlayer.uniqueId) {
                            return@get call.respond(HttpStatusCode.Unauthorized, "You are not the owner of this shop")
                        }

                        val shopSetting = ShopSetting(
                            shop.price,
                            if (shop.isBuying) ShopMode.BUY else ShopMode.SELL,
                            shop.shopStackingAmount
                        )
                        call.respond(shopSetting)
                    }

                    post {
                        //It cannot be set limit price
                        val data = call.receive<ShopSetting>()
                        val shopId = call.parameters["shopId"]?.toLongOrNull()
                            ?: return@post call.respond("Invalid shop id")
                        val shop = QuickShopIntegration.quickshop.shopManager.getShop(shopId)
                            ?: return@post call.respond("Shop not found")

                        val principal = call.principal<JWTPrincipal>()
                        val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                        val offlinePlayer = uuid.toUUID().toOfflinePlayer()

                        if (shop.owner.uniqueId != offlinePlayer.uniqueId) {
                            return@post call.respond(HttpStatusCode.Unauthorized, "You are not the owner of this shop")
                        }

                        // set price
                        if (data.price <= 0) {
                            return@post call.respond(HttpStatusCode.BadRequest, "Price must be greater than 0")
                        }

                        shop.price = data.price

                        // set shop type
                        shop.shopType = if (data.mode == ShopMode.BUY) ShopType.BUYING else ShopType.SELLING

                        // shop.shopStackingAmount
                        val item = shop.item
                        if(data.perBulkAmount <= 0){
                            return@post call.respond(HttpStatusCode.BadRequest, "perBulkAmount must be greater than 0")
                        }else if(data.perBulkAmount > getItemMaxStackSize(item.type)){
                            return@post call.respond(HttpStatusCode.BadRequest, "perBulkAmount must be less than or equal to ${getItemMaxStackSize(item.type)}")
                        }
                        val pendingItemStack = item.clone()
                        pendingItemStack.amount = data.perBulkAmount
                        shop.item = pendingItemStack

                        call.respond(HttpStatusCode.OK)
                    }
                }
            }
            get("/users/{uuid}/shops") {
                val uuid = call.parameters["uuid"]?.toUUID()
                    ?: return@get call.respond("Invalid uuid")
                val offlinePlayer = uuid.toOfflinePlayer()

                val data = QuickShopIntegration.quickshop.shopManager.allShops.filter {
                    it.owner.uniqueId == offlinePlayer.uniqueId
                }.map {
                    it.shopId
                }
                call.respond(data)
            }
            get("/shops/{shopId}") {
                val shopId = call.parameters["shopId"]?.toLongOrNull()
                    ?: return@get call.respond("Invalid shop id")
                val shop = QuickShopIntegration.quickshop.shopManager.getShop(shopId)
                    ?: return@get call.respond("Shop not found")

                val shopData = ShopData(
                    shop.shopId,
                    shop.owner.uniqueId,
                    if (shop.isBuying) ShopMode.BUY else ShopMode.SELL,
                    shop.shopStackingAmount,
                    getRemaining(shop),
                    LocationData.fromLocation(shop.location),
                    shop.price,
                    ItemStackData.fromItemStack(shop.item),
                )
                call.respond(shopData)
            }
        }
    }

    private suspend fun getRemaining(shop: Shop): Int = withContext(Dispatchers.minecraft) {
        if (shop.isBuying) {
            shop.remainingSpace
        } else {
            shop.remainingStock
        }
    }

    private fun getItemMaxStackSize(material: Material ): Int {
        return material.maxStackSize
    }
}