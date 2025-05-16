package party.morino.mineauth.api.http

import party.morino.mineauth.api.annotations.HttpHandler

/**
 * MineAuthのHTTP APIを定義するインターフェース
 */
interface HttpApi {
    /**
     * クラス内のHTTPハンドラーを登録します
     * @param handlerInstance ハンドラーが定義されたクラスのインスタンス
     */
    fun registerHandlers(handlerInstance: Any)

    /**
     * エンドポイントを追加します
     * @param path パス
     * @param method HTTPメソッド
     * @param handler ハンドラー関数
     */
    fun addEndpoint(path: String, method: HttpMethod, handler: (HttpRequest) -> HttpResponse)

    /**
     * ミドルウェアを追加します
     * @param middleware ミドルウェア関数
     */
    fun addMiddleware(middleware: (HttpRequest) -> HttpResponse?)
}

/**
 * HTTPメソッドを表す列挙型
 */
enum class HttpMethod {
    GET, POST, PUT, DELETE, PATCH
}

/**
 * HTTPリクエストを表すデータクラス
 * @property parameters URLパラメータ
 * @property body リクエストボディ
 * @property headers リクエストヘッダー
 */
data class HttpRequest(
    val parameters: Map<String, String>,
    val body: String?,
    val headers: Map<String, String>
)

/**
 * HTTPレスポンスを表すデータクラス
 * @property status HTTPステータスコード
 * @property body レスポンスボディ
 * @property headers レスポンスヘッダー
 */
data class HttpResponse(
    val status: Int,
    val body: String?,
    val headers: Map<String, String> = mapOf()
) 