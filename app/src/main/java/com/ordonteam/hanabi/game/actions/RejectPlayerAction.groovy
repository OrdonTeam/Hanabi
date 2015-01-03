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
        HanabiPlayer activePlayer = game.players.get(this.sourcePlayer)
        game.rejectedCards.add(activePlayer.cardsOnHand.get(this.card))
        activePlayer.cardsOnHand.add(game.getCardFromStack())
        if(game.tipsNumber <= 7){
            game.tipsNumber++
        }

        return game.isGameFinished()
    }

}