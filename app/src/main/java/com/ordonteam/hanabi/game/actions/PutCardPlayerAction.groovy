package com.ordonteam.hanabi.game.actions


class PutCardPlayerAction extends BasePlayerAction{

    private Integer card

    void setCard(int theCard){
        card = theCard
    }

    Integer getCard() {
        return card
    }

    public static class aPutCardPlayerAction {

        private PutCardPlayerAction built

        public aPutCardPlayerAction(){
            built = new PutCardPlayerAction()
        }

        public aPutCardPlayerAction withSourcePlayer(int player){
            built.sourcePlayer = player
            return this
        }

        public aPutCardPlayerAction withPuttedCard(int card){
            built.card = card
            return this
        }
    }
}
