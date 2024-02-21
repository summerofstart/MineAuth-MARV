package party.morino.moripaapi.commands

import be.seeseemelk.mockbukkit.ServerMock
import party.morino.moripaapi.TemplateTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.koin.test.KoinTest
import org.koin.test.inject

@ExtendWith(TemplateTest::class)
class HelpCommandTest :KoinTest{
    private val server : ServerMock by inject()

    @Test
    @DisplayName("command test: plugin-template help")
    fun sendHelp(){
        val player = server.addPlayer()
        val result  = player.performCommand("plugin-template help")
        assert(result)
    }
}