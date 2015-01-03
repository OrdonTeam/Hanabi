package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiCard
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic

@CompileStatic
class PutCardPlayerAction extends BasePlayerAction{

    Integer card

    PutCardPlayerAction(Integer card,Integer sourcePlayer) {
        this.card = card
        this.sourcePlayer = sourcePlayer
    }

    @Override
    boolean doAction(HanabiGame game) {
        HanabiPlayer activePlayer = game.getPlayerAt(this.sourcePlayer)
        HanabiCard playedCard = activePlayer.removeCardAt(this.card)

        game.addPlayerCard(playedCard)
        activePlayer.getCardFromStack(game)

        if(!game.isLowerCardWithTheSameColorOnTable(playedCard)){
            game.makeThunder()
        }
        return game.isGameFinished()
    }

}
