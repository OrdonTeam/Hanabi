package com.ordonteam.hanabi.game.actions


class RejectPlayerAction extends BasePlayerAction{

    private Integer card

    void setRejectedCard(int theCard){
        card = theCard
    }

    Integer getCard() {
        return card
    }

    static class aRejectPlayerAction {

        private RejectPlayerAction built

        public aRejectPlayerAction(){
            built = new RejectPlayerAction()
        }

        public aRejectPlayerAction withSourcePlayer(int player){
            built.sourcePlayer = player
            return this
        }

        public aRejectPlayerAction withRejectedCard(int theCard){
            built.card = theCard
            return this
        }
    }
}
