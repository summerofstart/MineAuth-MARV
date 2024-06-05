package party.morino.mineauth.core.web.router.plugin.vault

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.component.KoinComponent
import party.morino.mineauth.core.integration.vault.VaultIntegration
import party.morino.mineauth.core.utils.PlayerUtils.toOfflinePlayer
import party.morino.mineauth.core.utils.PlayerUtils.toUUID
import party.morino.mineauth.core.web.JwtCompleteCode
import party.morino.mineauth.core.web.router.plugin.vault.data.RemittanceData

object VaultRouter : KoinComponent {

    fun Route.vaultRouter() {
        route("/vault") {
            authenticate(JwtCompleteCode.USER_TOKEN.code) {
                get("/balance/me") {
                    val principal = call.principal<JWTPrincipal>()
                    val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                    val offlinePlayer = uuid.toUUID().toOfflinePlayer()
                    VaultIntegration.economy.getBalance(offlinePlayer).let {
                        call.respond(mapOf("balance" to it))
                    }
                }
                post("/send") {
                    val principal = call.principal<JWTPrincipal>()
                    val uuid = principal!!.payload.getClaim("playerUniqueId").asString()
                    val sender = uuid.toUUID().toOfflinePlayer()
                    val data = call.receive<RemittanceData>()
                    val target = data.target
                    val amount = data.amount

                    val economy = VaultIntegration.economy
                    if (amount <= 0) {
                        call.respond(HttpStatusCode.BadRequest,"Invalid amount")
                    }
                    if (economy.getBalance(sender) < amount) {
                        call.respond(HttpStatusCode.BadRequest,"Insufficient balance")
                    } else {
                        economy.withdrawPlayer(sender, amount)
                        economy.depositPlayer(target, amount)
                        val balance = economy.getBalance(sender)
                        val targetName = target.name
                        call.respond("Successfully sent $amount to $targetName. Your balance is $balance")
                }}
            }
        }
    }
}