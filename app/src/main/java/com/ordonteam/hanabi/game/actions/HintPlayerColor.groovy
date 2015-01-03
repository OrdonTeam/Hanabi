package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.CardColor
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerColor extends BasePlayerAction {

    Integer destinationPlayer
    Integer indexCardColor

    HintPlayerColor(Integer destinationPlayer, Integer indexCardColor, Integer sourcePlayer) {
        this.destinationPlayer = destinationPlayer
        this.indexCardColor = indexCardColor
        this.sourcePlayer = sourcePlayer
    }

    @Override
    boolean doAction(HanabiGame game) {
        HanabiPlayer destinationPlayer = game.getPlayerAt(this.destinationPlayer)
        CardColor color = destinationPlayer.getColorOf(this.indexCardColor)
        destinationPlayer.hintColor(color)
        game.tipsNumber--

        return false
    }
}
