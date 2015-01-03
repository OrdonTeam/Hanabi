package com.ordonteam.hanabi.game

import android.graphics.Color
import groovy.transform.CompileStatic

@CompileStatic
public enum CardColor {
    RED(Color.RED), YELLOW(Color.YELLOW), BLUE(Color.BLUE), MAGENTA(Color.MAGENTA), GREEN(Color.GREEN)

    int color

    CardColor(int color){
        this.color = color
    }
}