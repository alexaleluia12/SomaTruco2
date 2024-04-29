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

    @Test
    fun viewModel_correct_show_win() {
        repeat(4) {
            vm.incByThreeForPlayerOne()
        }
        val currentStateUi = vm.uiState

        assertEquals(0, currentStateUi.playerOne.count)
        assertEquals(1, currentStateUi.playerOne.winCount)
    }

    @Test
    fun viewModel_update_name_does_not_affect_other() {
        val newName = "Bob"
        val prevPlayerTwoName = vm.uiState.playerTwo.name
        vm.editTarget = vm.playerOne
        vm.changeNameTo(newName)
        val currentStateUi = vm.uiState

        assertEquals(newName, currentStateUi.playerOne.name)
        assertEquals(prevPlayerTwoName, currentStateUi.playerTwo.name)
    }
}