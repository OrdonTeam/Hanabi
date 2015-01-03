package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.HanabiPlayer
import groovy.transform.CompileStatic

@CompileStatic
class RejectPlayerAction extends BasePlayerAction {

    Integer card

    RejectPlayerActionBuilder aRejectPlayerAction(){
        return new RejectPlayerActionBuilder()
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

    public static class RejectPlayerActionBuilder {

        private RejectPlayerAction built

        public RejectPlayerActionBuilder() {
            built = new RejectPlayerAction()
        }

        public RejectPlayerActionBuilder withSourcePlayer(int player) {
            built.sourcePlayer = player
            return this
        }

        public RejectPlayerActionBuilder withRejectedCard(int theCard) {
            built.card = theCard
            return this
        }
    }
}