package party.morino.moripaapi.web.router.auth

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.velocity.*

object LoginRouter {
    fun Route.loginRouter() {
        route("/login") {
            get { // 認証画面を返す
                val model = mapOf<String, String>()
                call.respond(VelocityContent("login.vm", model))
            }
        }
    }
}