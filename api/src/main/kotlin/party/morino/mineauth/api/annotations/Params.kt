package party.morino.mineauth.api.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Params(
    val value: Array<String>
)
