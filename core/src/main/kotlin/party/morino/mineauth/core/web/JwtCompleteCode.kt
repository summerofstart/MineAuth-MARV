package party.morino.mineauth.core.web

enum class JwtCompleteCode(val code: String) {
    USER_TOKEN("user-oauth-token"),
    SERVICE_TOKEN("service-oauth-token")
}