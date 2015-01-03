package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiCard
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic

@CompileStatic
class PutCardPlayerAction extends BasePlayerAction{

    Integer card

    static PutCardPlayerActionBuilder aPutPlayerAction(){
        return new PutCardPlayerActionBuilder()
    }

    @Override
    boolean doAction(HanabiGame game) {
        HanabiPlayer activePlayer = game.players.get(this.sourcePlayer)
        HanabiCard playedCard = activePlayer.cardsOnHand.get(this.card)
        game.playedCards.add(playedCard)
        activePlayer.cardsOnHand.remove(playedCard)
        activePlayer.cardsOnHand.add(game.getCardFromStack())

        if(!game.isLowerCardWithTheSameColorOnTable(playedCard)){
            game.makeThunder()
        }
        return game.isGameFinished()
    }

    static class PutCardPlayerActionBuilder {

        private PutCardPlayerAction built

        PutCardPlayerActionBuilder(){
            built = new PutCardPlayerAction()
        }

        PutCardPlayerActionBuilder withSourcePlayer(int player){
            built.sourcePlayer = player
            return this
        }

        PutCardPlayerActionBuilder withPuttedCard(int card){
            built.card = card
            return this
        }

        PutCardPlayerAction build(){
            return built
        }
    }
}
