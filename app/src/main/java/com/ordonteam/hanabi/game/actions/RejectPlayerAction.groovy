package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiCard
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic

@CompileStatic
class RejectPlayerAction extends BasePlayerAction {

    Integer card

    RejectPlayerAction(Integer card,Integer sourcePlayer) {
        this.card = card
        this.sourcePlayer = sourcePlayer
    }

    @Override
    boolean doAction(HanabiGame game) {
        HanabiPlayer activePlayer = game.getPlayerAt(sourcePlayer)
        HanabiCard rejectedCard = activePlayer.removeCardAt(card)

        game.rejectedCards.add(rejectedCard)
        activePlayer.getCardFromStack(game)

        if(game.tipsNumber <= 7){
            game.tipsNumber++
        }

        return game.isGameFinished()
    }

}