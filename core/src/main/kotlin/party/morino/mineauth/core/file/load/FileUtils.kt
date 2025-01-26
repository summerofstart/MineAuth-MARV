package party.morino.mineauth.core.file.load

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth
import party.morino.mineauth.core.database.UserAuthData
import party.morino.mineauth.core.file.load.config.OAuthConfigLoader
import party.morino.mineauth.core.file.load.config.WebConfigLoader
import party.morino.mineauth.core.file.load.resources.AssetsResourceLoader
import party.morino.mineauth.core.file.load.resources.TemplatePageResourceLoader
import party.morino.mineauth.core.file.utils.KeyUtils
import java.io.File

object FileUtils: KoinComponent {
    private val plugin: MineAuth by inject()
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
            url = "jdbc:sqlite:${plugin.dataFolder}${File.separator}MineAuth.db", driver = "org.sqlite.JDBC"
        )

        transaction {
            SchemaUtils.create(UserAuthData)
        }
        plugin.logger.info("Database connected")
    }
}