package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerNumber extends BasePlayerAction{

    Integer destinationPlayer
    Integer indexCardNumber

    HintPlayerNumber(Integer destinationPlayer, Integer indexCardNumber, Integer sourcePlayer) {
        this.destinationPlayer = destinationPlayer
        this.indexCardNumber = indexCardNumber
        this.sourcePlayer = sourcePlayer
    }

    @Override
    boolean doAction(HanabiGame game) {
        HanabiPlayer activePlayer = game.players.get(this.sourcePlayer)

        return false
    }
}
