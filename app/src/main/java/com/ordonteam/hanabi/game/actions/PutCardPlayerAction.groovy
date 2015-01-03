package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic

@CompileStatic
class PutCardPlayerAction extends BasePlayerAction{

    Integer card

    static PutCardPlayerActionBuilder aPutPlayerAction(){
        return new PutCardPlayerActionBuilder()
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
