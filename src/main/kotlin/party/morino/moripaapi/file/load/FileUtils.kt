package party.morino.moripaapi.file.load

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.database.UserAuthData
import party.morino.moripaapi.file.load.config.OAuthConfigLoader
import party.morino.moripaapi.file.load.config.WebConfigLoader
import party.morino.moripaapi.file.load.resources.AssetsResourceLoader
import party.morino.moripaapi.file.load.resources.TemplatePageResourceLoader
import party.morino.moripaapi.file.utils.KeyUtils
import java.io.File

object FileUtils: KoinComponent {
    private val plugin: MoripaAPI by inject()
    fun loadFiles() {
        KeyUtils.init()
        val loaders = listOf<FileLoaderInterface>(
            WebConfigLoader(), OAuthConfigLoader(), AssetsResourceLoader(), TemplatePageResourceLoader()
        )

        loaders.stream().forEach {
            it.load()
        }
    }

    fun settingDatabase() {
        Database.connect(
            url = "jdbc:sqlite:${plugin.dataFolder}${File.separator}Moripa-API.db", driver = "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(UserAuthData)
        }
        plugin.logger.info("Database connected")
    }
}