package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic

@CompileStatic
class RejectPlayerAction extends BasePlayerAction {

    Integer card

    RejectPlayerActionBuilder aRejectPlayerAction(){
        return new RejectPlayerActionBuilder()
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