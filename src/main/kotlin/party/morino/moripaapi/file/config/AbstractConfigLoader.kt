package party.morino.moripaapi.file.config

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import java.io.File

abstract class AbstractConfigLoader: KoinComponent {
    val plugin: MoripaAPI by inject()
    abstract val configFile: File
    abstract fun load()
}