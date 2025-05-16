package party.morino.mineauth.addons.quickshop

import party.morino.mineauth.api.http.HttpApi

class QuickShopAddon(
    private val httpApi: HttpApi
) {
    fun initialize() {
        // ハンドラーを登録
        httpApi.registerHandlers(ShopHandler())
    }
} 