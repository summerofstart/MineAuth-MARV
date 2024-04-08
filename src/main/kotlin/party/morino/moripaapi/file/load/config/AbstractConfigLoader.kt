package party.morino.moripaapi.file.load.config

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.load.FileLoaderInterface
import java.io.File

abstract class AbstractConfigLoader: KoinComponent, FileLoaderInterface {
    protected val plugin: MoripaAPI by inject()
    protected abstract val configFile: File
    abstract override fun load()
}