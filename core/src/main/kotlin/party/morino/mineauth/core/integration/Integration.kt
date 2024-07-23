package party.morino.mineauth.core.integration

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth

abstract class Integration: KoinComponent {
    val mineAuth: MineAuth by inject()
    open var available: Boolean = false
    abstract val name: String

    open fun initialize() {}
}