package com.ordonteam.hanabi.game.actions


class RejectPlayerAction extends BasePlayerAction{

    void setRejectedCard(int card){

    }

    static class aRejectPlayerAction {

        private RejectPlayerAction built

        aRejectPlayerAction(){
            built = new RejectPlayerAction()
        }

        RejectPlayerAction withSourcePlayer(int player){
            return built
        }
    }
}
