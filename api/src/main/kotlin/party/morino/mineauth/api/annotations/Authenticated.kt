package party.morino.mineauth.api.annotations

/**
 * 認証されたプレイヤーを関数の引数として受け取るためのアノテーション
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Authenticated