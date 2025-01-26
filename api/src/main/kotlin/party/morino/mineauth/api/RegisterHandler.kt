package party.morino.mineauth.api


interface RegisterHandler {
    fun register(vararg endpoints: Any): RegisterHandler
}
