package party.morino.mineauth.core.integration

interface Integration {
    var available: Boolean
    val name: String

    fun initialize()
}