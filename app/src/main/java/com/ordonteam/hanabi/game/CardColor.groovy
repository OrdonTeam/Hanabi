package com.ordonteam.hanabi.game

import android.graphics.Color
import groovy.transform.CompileStatic

@CompileStatic
public enum CardColor implements Serializable{
    RED(Color.RED, 0), YELLOW(Color.YELLOW, 1), BLUE(Color.BLUE, 2), MAGENTA(Color.MAGENTA, 3), GREEN(Color.GREEN, 4), EMPTY(Color.TRANSPARENT, 0)
    static final long serialVersionUID = 42L;

    int color
    int placeOnBoard

    CardColor(int color, Integer placeOnBoard){
        this.color = color
        this.placeOnBoard = placeOnBoard
    }

    static List<CardColor> colors(){
        return [RED,YELLOW,BLUE,MAGENTA,GREEN]
    }
}