package party.morino.mineauth.api.http

/**
 * HTTPエラーを表すクラス
 * @property status HTTPステータス
 * @property message エラーメッセージ
 * @property details エラーの詳細情報
 */
class HttpError(
    val status: HttpStatus,
    val message: String,
    val details: Map<String, Any> = mapOf()
) : RuntimeException(message) 