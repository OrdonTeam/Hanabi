package com.ordonteam.hanabi.game.actions

abstract class BasePlayerAction {

    Integer getSourcePlayer() {
        return sourcePlayer
    }
    protected Integer sourcePlayer

    void setSourcePlayer( int player){
        sourcePlayer = player
    }

}
