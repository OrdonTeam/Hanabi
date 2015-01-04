package com.ordonteam.hanabi.game

import android.graphics.Color
import groovy.transform.CompileStatic

@CompileStatic
public enum CardColor {
    RED(Color.RED, 0), YELLOW(Color.YELLOW, 1), BLUE(Color.BLUE, 2), MAGENTA(Color.MAGENTA, 3), GREEN(Color.GREEN, 4)

    int color
    int placeOnBoard

    CardColor(int color, Integer placeOnBoard){
        this.color = color
        this.placeOnBoard = placeOnBoard
    }
}