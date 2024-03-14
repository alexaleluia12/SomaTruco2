package com.alexaleluia12.somatruco2

import com.alexaleluia12.somatruco2.data.Player

class CountController(val player1: Player, val player2: Player) {
    object GameProperties {
        const val MAX_COUNT = 12
        const val ALERT_LIMIT = 11

    }

    fun verifyWinCondition(): Boolean {
        if (playerHasWin(player1)){
            println(player1.name + " win!")
            player2.count = 0
            return true
        } else if(playerHasWin(player2)) {
            player1.count = 0

            println(player2.name + " win!")
            return true
        }
        return false
    }

    private fun playerHasWin(player: Player): Boolean {
        return if (player.count >= GameProperties.MAX_COUNT) {
            player.apply {
                winCount += 1
                count = 0
            }
            true
        } else {
            false
        }
    }

    fun addThree(player: Player):Boolean { // podia usar delegate e jogar esse metodos no Player
        player.count += 3
        return verifyWinCondition()
    }

    fun addOne(player: Player):Boolean {
        player.count += 1
        return verifyWinCondition()
    }

    fun minusOne(player: Player) {
        player.count -= 1
    }

}