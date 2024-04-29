package com.alexaleluia12.somatruco2

import com.alexaleluia12.somatruco2.data.Player

class CountController(val player1: Player, val player2: Player) {
    object GameProperties {
        const val MAX_COUNT = 12
        const val ALERT_LIMIT = 11

    }

    fun verifyWinCondition(): Boolean {
        if (player1.count >= GameProperties.ALERT_LIMIT && !player1.closeToWin) {
            player1.closeToWin = true
        }
        if (player2.count >= GameProperties.ALERT_LIMIT && !player2.closeToWin) {
            player2.closeToWin = true
        }

        if (playerHasWin(player1)){
            player2.count = 0
            player2.closeToWin = false
            return true
        } else if(playerHasWin(player2)) {
            player1.count = 0
            player1.closeToWin = false
            return true
        }
        return false
    }

    private fun playerHasWin(player: Player): Boolean {
        return if (player.count >= GameProperties.MAX_COUNT) {
            player.apply {
                winCount += 1
                count = 0
                closeToWin = false
            }
            true
        } else {
            false
        }
    }

    fun resetAll() {
        player1.apply {
            winCount = 0
            count = 0
            closeToWin = false
        }
        player2.apply {
            winCount = 0
            count = 0
            closeToWin = false
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