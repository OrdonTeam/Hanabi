package com.ordonteam.hanabi.game.actions


class PutCardPlayerAction extends BasePlayerAction{

    void setCard(int card){

    }

    static class aPutCardPlayerAction {

        private PutCardPlayerAction built

        aPutCardPlayerAction(){
            built = new PutCardPlayerAction()
        }

        PutCardPlayerAction withSourcePlayer(int player){
            return built
        }
    }
}
