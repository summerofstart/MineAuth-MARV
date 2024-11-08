package party.morino.mineauth.addon.routes

import org.bukkit.entity.Player
import party.morino.mineauth.api.annotations.Path
import party.morino.mineauth.api.annotations.Permission

// plugin名がmineauth-api-quickshop-hikari-addonなので plugins/mineauth-api-quickshop-hikari-addon/shopsにアクセスするとここに飛ぶ
@Path("/shops")
class ShopRoute {

    @Path("/id/:id")
    @Permission("quickshop-hikari.shop")
    fun user(player: Player, id : String) {

    }
}