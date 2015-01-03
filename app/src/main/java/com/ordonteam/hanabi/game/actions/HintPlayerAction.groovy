package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerAction extends BasePlayerAction{

    Integer destinationPlayer
    Integer cardColor
    Integer cardNumber


    static HintPlayerActionBuilder aHintPlayerAction(){
        return new HintPlayerActionBuilder()
    }

    static class HintPlayerActionBuilder {

        private HintPlayerAction built

        public HintPlayerActionBuilder(){
            built = new HintPlayerAction()
        }

        public HintPlayerActionBuilder withSourcePlayer(int player){
            built.sourcePlayer = player
            return this
        }

        public HintPlayerActionBuilder withDestinationPlayer(int player){
            built.destinationPlayer = player
            return this
        }

        public HintPlayerActionBuilder withCardColor(int color){
            built.cardColor = color
            return this
        }

        public HintPlayerActionBuilder withCardNumber(int number){
            built.cardNumber = number
            return this
        }

        public HintPlayerAction build(){
            return built
        }
    }
}
