package com.alexaleluia12.somatruco2

import com.alexaleluia12.somatruco2.data.Player
import org.junit.Assert.*
import org.junit.Test

class CountControllerTest {

    @Test
    fun `flow for playerA win`() {
        val playerA = Player(name = "A")
        val playerB = Player(name = "B")
        val countControler = CountController(playerA, playerB)
        countControler.addOne(playerB)
        repeat(4) {
            countControler.addThree(playerA)
        }
        assertEquals("Player win a match",1, playerA.winCount)
        assertEquals("After win a match count starts from zero",0, playerA.count)

        assertEquals(0, playerB.winCount)
        assertEquals("The other player wins so points are restarted",0, playerB.count)
    }

    @Test
    fun `correct reset data of all players`() {
        val playerA = Player(name = "A")
        val playerB = Player(name = "B")
        val countControler = CountController(playerA, playerB)
        countControler.addOne(playerB)
        repeat(4) {
            countControler.addThree(playerA)
        }
        repeat(4) {
            countControler.addThree(playerB)
        }
        countControler.resetAll()
        assertEquals(0, playerA.count)
        assertEquals(0, playerA.winCount)

        assertEquals(0, playerB.count)
        assertEquals(0, playerB.winCount)
    }
}