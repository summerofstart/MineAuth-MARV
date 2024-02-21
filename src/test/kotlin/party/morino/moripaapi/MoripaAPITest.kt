package party.morino.moripaapi

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback

import org.junit.jupiter.api.extension.ExtensionContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.dsl.module

open class MoripaAPITest : BeforeEachCallback, AfterEachCallback {

    private lateinit var server: ServerMock
    private lateinit var plugin: MoripaAPI

    override fun beforeEach(context: ExtensionContext) {
        println("beforeEach() executed before " + context.displayName + ".");
        server = MockBukkit.mock()
        plugin = MockBukkit.load(MoripaAPI::class.java)
        setupKoin()
    }

    override fun afterEach(context: ExtensionContext) {
        MockBukkit.unmock()
        stopKoin()
    }


    private fun setupKoin() {

        val appModule = module {
            single<MoripaAPI> { plugin }
            single<ServerMock> { server }
        }
        loadKoinModules(appModule)
    }

}