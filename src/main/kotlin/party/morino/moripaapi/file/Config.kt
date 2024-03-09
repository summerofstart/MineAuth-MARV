package party.morino.moripaapi.file

import kotlinx.serialization.encodeToString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.config.WebServerConfigData
import party.morino.moripaapi.file.utils.KeyUtils
import party.morino.moripaapi.utils.json

object Config: KoinComponent {
    private val plugin: MoripaAPI by inject()
    fun loadConfig() {
        KeyUtils.init()
        val file = plugin.dataFolder.resolve("config.json")
        if (!file.exists()) {
            plugin.logger.info("Config file not found. Creating new one.")
            file.parentFile.mkdirs()
            file.createNewFile()
            val configData = WebServerConfigData(8080, null)
            file.writeText(json.encodeToString(configData))
        }
        val configData: WebServerConfigData = json.decodeFromString(file.readText())
        loadKoinModules(module {
            single { configData }
        })

    }
}