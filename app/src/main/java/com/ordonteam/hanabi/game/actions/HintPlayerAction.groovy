package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerAction extends BasePlayerAction{

    Integer destinationPlayer
    Integer cardColor
    Integer cardNumber


//    static class aHintPlayerAction {
//
//        private HintPlayerAction built
//
//        public aHintPlayerAction(){
//            built = new HintPlayerAction()
//        }
//
//        public aHintPlayerAction withSourcePlayer(int player){
//            built.sourcePlayer = player
//            return this
//        }
//
//        public aHintPlayerAction withDestinationPlayer(int player){
//            built.destinationPlayer = player
//            return this
//        }
//
//        public aHintPlayerAction withCardColor(int color){
//            built.cardColor = color
//            return this
//        }
//
//        public aHintPlayerAction withCardNumber(int number){
//            built.cardNumber = number
//            return this
//        }
//    }
}
