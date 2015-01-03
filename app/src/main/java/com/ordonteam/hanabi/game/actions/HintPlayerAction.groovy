package com.ordonteam.hanabi.game.actions

class HintPlayerAction extends BasePlayerAction{

    void setDestinationPlayer(int player){

    }

    static class aHintPlayerAction {

        private HintPlayerAction built

        aHintPlayerAction(){
            built = new HintPlayerAction()
        }

        HintPlayerAction withSourcePlayer(int player){
            return built
        }
    }
}
