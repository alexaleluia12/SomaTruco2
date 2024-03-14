package com.alexaleluia12.somatruco2

import com.alexaleluia12.somatruco2.data.GameViewModel
import org.junit.Assert.*
import org.junit.Test

class GameSmokeInstrumentalTest {
    val vm = GameViewModel()

    @Test
    fun viewModel_correct_increase_points() {
        vm.incByOneForPlayerOne()
        vm.incByThreeForPlayerTwo()
        val currentStateUi = vm.uiState

        assertEquals(1, currentStateUi.playerOne.count)
        assertEquals(3, currentStateUi.playerTwo.count)
    }
}