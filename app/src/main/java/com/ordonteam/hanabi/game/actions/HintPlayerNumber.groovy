package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.CardValue
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
        HanabiPlayer destinationPlayer = game.players.get(this.destinationPlayer)
        CardValue value = destinationPlayer.getValueOf(this.indexCardNumber)
        destinationPlayer.hintNumber(value)
        game.tipsNumber--
        return false
    }
}
