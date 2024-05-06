package party.morino.mineauth.core.file.load.config

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth
import party.morino.mineauth.core.file.load.FileLoaderInterface
import java.io.File

abstract class AbstractConfigLoader: KoinComponent, FileLoaderInterface {
    protected val plugin: MineAuth by inject()
    protected abstract val configFile: File
    abstract override fun load()
}