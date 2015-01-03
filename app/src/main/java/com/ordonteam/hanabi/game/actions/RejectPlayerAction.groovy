package com.ordonteam.hanabi.game.actions

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
        HanabiPlayer activePlayer = game.players.get(sourcePlayer)
        game.rejectedCards.add(activePlayer.removeCardAt(card))
        activePlayer.getCardFromStack(game)
        if(game.tipsNumber <= 7){
            game.tipsNumber++
        }

        return game.isGameFinished()
    }

}