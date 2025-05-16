package party.morino.mineauth.api.annotations

import party.morino.mineauth.api.http.HttpMethod

/**
 * HTTPハンドラーを定義するためのアノテーション
 * @param path エンドポイントのパス
 * @param method HTTPメソッド（デフォルトはGET）
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class HttpHandler(
    val path: String,
    val method: HttpMethod = HttpMethod.GET
)