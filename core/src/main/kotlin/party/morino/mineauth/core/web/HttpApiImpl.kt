package party.morino.mineauth.core.web

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import kotlin.reflect.full.*
import party.morino.mineauth.api.http.*
import party.morino.mineauth.api.annotation.*
import party.morino.mineauth.api.annotations.Authenticated
import party.morino.mineauth.api.annotations.HttpHandler

/**
 * HttpApiの実装クラス
 * アノテーションを利用してHTTPルートを登録します
 */
class HttpApiImpl(
    private val application: Application
) : HttpApi {
    override fun registerHandlers(handlerInstance: Any) {
        val kClass = handlerInstance::class
        val functions = kClass.functions.filter { it.findAnnotation<HttpHandler>() != null }

        application.routing {
            functions.forEach { function ->
                val annotation = function.findAnnotation<HttpHandler>()!!
                val path = annotation.path
                val method = annotation.method.toKtorMethod()

                route(path, method) {
                    handle {
                        // 引数の解決
                        val params = function.parameters.map { parameter ->
                            when {
                                parameter.findAnnotation<Authenticated>() != null -> {
                                    // 認証されたプレイヤーを取得
                                    getAuthenticatedPlayer(call)
                                }
                                else -> null // 必要に応じて他の引数を処理
                            }
                        }
                        // 関数の呼び出し
                        val result = function.callSuspend(handlerInstance, *params.toTypedArray())
                        call.respond(result ?: "")
                    }
                }
            }
        }
    }

    /**
     * 認証されたプレイヤーを取得します
     */
    private suspend fun getAuthenticatedPlayer(call: ApplicationCall): Player {
        // 認証処理を実装してください
        // 例として仮のPlayerオブジェクトを返します
        return Player("playerName")
    }

    /**
     * HttpMethodをKtorのHttpMethodに変換します
     */
    private fun HttpMethod.toKtorMethod(): io.ktor.http.HttpMethod {
        return when (this) {
            HttpMethod.GET -> io.ktor.http.HttpMethod.Get
            HttpMethod.POST -> io.ktor.http.HttpMethod.Post
            HttpMethod.PUT -> io.ktor.http.HttpMethod.Put
            HttpMethod.DELETE -> io.ktor.http.HttpMethod.Delete
            HttpMethod.PATCH -> io.ktor.http.HttpMethod.Patch
        }
    }
} 