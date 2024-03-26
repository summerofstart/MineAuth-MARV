package party.morino.moripaapi.file.config

import kotlinx.serialization.encodeToString
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import party.morino.moripaapi.file.data.WebServerConfigData
import party.morino.moripaapi.utils.json
import java.io.File

class WebConfigLoader: AbstractConfigLoader() {
    override val configFile: File = plugin.dataFolder.resolve("config").resolve("web-server.json")

    override fun load() {
        if (!configFile.exists()) {
            plugin.logger.info("${configFile.name} not found. Creating new one.")
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
            val configData = WebServerConfigData(8080, null)
            configFile.writeText(json.encodeToString(configData))
        }
        val configData: WebServerConfigData = json.decodeFromString(configFile.readText())
        loadKoinModules(module {
            single { configData }
        })
    }
}