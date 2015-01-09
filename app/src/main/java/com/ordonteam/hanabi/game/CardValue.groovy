package com.ordonteam.hanabi.game

public enum CardValue implements Serializable {
    ZERO('0',0){
        @Override
        boolean inNext(CardValue cardValue) {
            return cardValue == ONE;
        }
    }, ONE('1', 3){
        @Override
        boolean inNext(CardValue cardValue) {
            return cardValue == TWO;
        }
    }, TWO('2', 2){
        @Override
        boolean inNext(CardValue cardValue) {
            return cardValue == THREE;
        }
    }, THREE('3', 2){
        @Override
        boolean inNext(CardValue cardValue) {
            return cardValue == FOUR;
        }
    }, FOUR('4', 2){
        @Override
        boolean inNext(CardValue cardValue) {
            return cardValue == FIVE;
        }
    }, FIVE('5', 1), EMPTY('',0)
    static final long serialVersionUID = 42L;

    String value
    int max

    CardValue(String value, int max) {
        this.value = value
        this.max = max
    }

    int amount() {
        return max
    }

    boolean inNext(CardValue cardValue) {
        return false;
    }
}