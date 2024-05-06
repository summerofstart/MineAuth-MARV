package party.morino.mineauth.core.file.load.resources

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.mineauth.core.MineAuth
import party.morino.mineauth.core.file.load.FileLoaderInterface
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class AssetsResourceLoader: KoinComponent, FileLoaderInterface {
    val plugin: MineAuth by inject()
    private val copyToDir = plugin.dataFolder.resolve("assets")
    private val files = listOf("lock.svg")

    override fun load() {
        plugin.logger.info("Loading assets...")
        if (!copyToDir.exists()) {
            copyToDir.mkdirs()
        }
        val resourceFolder = "/assets"

        files.forEach {
            val targetPath = copyToDir.resolve(it).toPath()
            val fileStream: InputStream = this::class.java.getResourceAsStream("$resourceFolder/$it")!!
            if (!targetPath.toFile().exists()) {
                Files.copy(fileStream, targetPath, StandardCopyOption.REPLACE_EXISTING)
            } else {
                plugin.logger.info("File $it already exists. Skipping.")
            }
        }
    }
}