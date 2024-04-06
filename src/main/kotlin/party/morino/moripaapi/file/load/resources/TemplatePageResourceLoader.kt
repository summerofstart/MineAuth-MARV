package party.morino.moripaapi.file.load.resources

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import party.morino.moripaapi.MoripaAPI
import party.morino.moripaapi.file.load.FileLoaderInterface
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class TemplatePageResourceLoader: KoinComponent, FileLoaderInterface {
    val plugin: MoripaAPI by inject()
    private val copyToDir = plugin.dataFolder.resolve("templates")
    private val files = listOf("authorize.vm")
    override fun load() {
        plugin.logger.info("Loading templates...")
        if (!copyToDir.exists()) {
            copyToDir.mkdirs()
        }
        val resourceFolder = "/templates"

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