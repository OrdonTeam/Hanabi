package com.ordonteam.hanabi.game

public enum CardValue {

    ONE(3), TWO(2), THREE(2), FOUR(2), FIVE(1)

    int max

    CardValue(int max) {
        this.max = max
    }
}