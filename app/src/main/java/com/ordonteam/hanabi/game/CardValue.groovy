package com.ordonteam.hanabi.game

public enum CardValue implements Serializable {
    ZERO(0,0), ONE(1, 3), TWO(2, 2), THREE(3, 2), FOUR(4, 2), FIVE(5, 1)
    static final long serialVersionUID = 42L;

    int value
    int max

    CardValue(int value, int max) {
        this.value = value
        this.max = max
    }

    int amount() {
        return max
    }
}