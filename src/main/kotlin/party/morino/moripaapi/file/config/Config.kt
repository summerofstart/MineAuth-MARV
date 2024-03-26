package party.morino.moripaapi.file.config

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.utils.KeyUtils

object Config: KoinComponent {
    private val plugin: MoripaAPI by inject()
    fun loadConfig() {
        KeyUtils.init()
        val loaders = listOf(
            WebConfigLoader(), OAuthConfigLoader()
        )

        loaders.stream().forEach {
            it.load()
        }
    }
}