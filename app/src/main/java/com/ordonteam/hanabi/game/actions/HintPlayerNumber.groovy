package com.ordonteam.hanabi.game.actions

import groovy.transform.CompileStatic


@CompileStatic
class HintPlayerNumber extends BasePlayerAction{

    Integer destinationPlayer
    Integer cardNumber

    HintPlayerNumber(Integer destinationPlayer, Integer cardNumber, Integer sourcePlayer) {
        this.destinationPlayer = destinationPlayer
        this.cardNumber = cardNumber
        this.sourcePlayer = sourcePlayer
    }
}
