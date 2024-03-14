package com.alexaleluia12.somatruco2.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.alexaleluia12.somatruco2.CountController

class GameViewModel: ViewModel() {
    private val p1 = Player(name = "P")
    private val p2 = Player(name = "Q")
    var uiState: GameState by mutableStateOf(GameState(p1.copy(), p2.copy()))
       private set

    private val counter = CountController(p1, p2)

    fun incByOneForPlayerOne() {
        val changeBoth = counter.addOne(p1)
        uiState = if (changeBoth) {
            // no caso de um vitoria é recessario atualizar o valor dos dois jogadores
            uiState.copy(playerOne = p1.copy(), playerTwo = p2.copy())
        } else {
            println("ref pura ${p1.count}")
            println("ref uiState ${uiState.playerOne.count}")
            uiState.copy(playerOne = p1.copy())
        }
    }

    fun incByOneForPlayerTwo() {
        val changeBoth = counter.addOne(p2)
        uiState = if (changeBoth) {
            uiState.copy(playerOne = p1.copy(), playerTwo = p2.copy())
        } else {
            println("ref pura ${p2.count}")
            println("ref uiState ${uiState.playerTwo.count}")
            uiState.copy(playerTwo = p2.copy())
        }
    }


    fun incByThreeForPlayerOne() {
        val changeBoth = counter.addThree(p1)
        uiState = if (changeBoth) {
            uiState.copy(playerOne = p1.copy(), playerTwo = p2.copy())
        } else {
            uiState.copy(playerOne = p1.copy())
        }
    }

    fun incByThreeForPlayerTwo() {
        val changeBoth = counter.addThree(p2)
        uiState = if (changeBoth) {
            uiState.copy(playerOne = p1.copy(), playerTwo = p2.copy())
        } else {
            uiState.copy(playerTwo = p2.copy())
        }

    }

    fun decByOneForPlayerOne() {
        counter.minusOne(p1)
        uiState = uiState.copy(playerOne = p1.copy())
    }

    // qnd aperta o -1 da esse log Debug: updateByCheckExcludeList
    fun decByOneForPlayerTwo() {
        counter.minusOne(p2)
        uiState = uiState.copy(playerTwo = p2.copy())
    }


}

data class GameState(
    val playerOne: Player,
    val playerTwo: Player
)