package party.morino.moripaapi.file.config

import kotlinx.serialization.encodeToString
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import party.morino.moripaapi.file.data.OAuthConfigData
import party.morino.moripaapi.utils.json
import java.io.File

class OAuthConfigLoader: AbstractConfigLoader() {
    override val configFile: File = plugin.dataFolder.resolve("config").resolve("oauth.json")

    override fun load() {
        if (!configFile.exists()) {
            plugin.logger.info("${configFile.name} not found. Creating new one.")
            configFile.parentFile.mkdirs()
            configFile.createNewFile()
            val configData = OAuthConfigData("Moripa-API", "/assets/lock.svg")
            configFile.writeText(json.encodeToString(configData))
        }
        val configData: OAuthConfigData = json.decodeFromString(configFile.readText())

        loadKoinModules(module {
            single { configData }
        })
    }
}