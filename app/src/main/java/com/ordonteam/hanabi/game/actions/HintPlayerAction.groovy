package com.ordonteam.hanabi.game.actions

class HintPlayerAction extends BasePlayerAction{

    private Integer destinationPlayer
    private Integer cardColor
    private Integer cardNumber

    void setDestinationPlayer(int player){
        destinationPlayer = player
    }

    void setCardColor( int color ){
        cardColor = color
    }

    void setCardNumber( int number){
        cardNumber = number
    }

    Integer getDestinationPlayer() {
        return destinationPlayer
    }

    Integer getCardColor() {
        return cardColor
    }

    Integer getCardNumber() {
        return cardNumber
    }

    static class aHintPlayerAction {

        private HintPlayerAction built

        public aHintPlayerAction(){
            built = new HintPlayerAction()
        }

        public aHintPlayerAction withSourcePlayer(int player){
            built.sourcePlayer = player
            return this
        }

        public aHintPlayerAction withDestinationPlayer(int player){
            built.destinationPlayer = player
            return this
        }

        public aHintPlayerAction withCardColor(int color){
            built.cardColor = color
            return this
        }

        public aHintPlayerAction withCardNumber(int number){
            built.cardNumber = number
            return this
        }
    }
}
