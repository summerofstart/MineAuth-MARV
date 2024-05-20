package party.morino.mineauth.api.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Parms(
    val value: Array<String>
)
