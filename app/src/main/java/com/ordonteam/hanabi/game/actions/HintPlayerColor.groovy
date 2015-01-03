package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerColor extends BasePlayerAction {

    Integer destinationPlayer
    Integer indexCardColor

    HintPlayerColor(Integer destinationPlayer, Integer indexCardColor, Integer sourcePlayer) {
        this.destinationPlayer = destinationPlayer
        this.indexCardColor = indexCardColor
        this.sourcePlayer = sourcePlayer
    }



}
