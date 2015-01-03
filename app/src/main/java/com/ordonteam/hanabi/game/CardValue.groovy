package com.ordonteam.hanabi.game

public enum CardValue {

    ONE(1, 3), TWO(2, 2), THREE(3, 2), FOUR(4, 2), FIVE(5, 1)

    int value
    int max

    CardValue(int value, int max) {
        this.value = value
        this.max = max
    }
}