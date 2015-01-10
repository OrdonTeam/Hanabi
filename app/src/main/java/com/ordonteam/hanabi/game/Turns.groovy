package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class Turns implements Serializable {
    static final long serialVersionUID = 42L;

    int tunsMadeAfterDeckEmpty = 0

    boolean takeTurn(Deck deck, Closure<Boolean> turn){
        boolean wasMade = turn()
        if(wasMade && deck.empty)
            tunsMadeAfterDeckEmpty++
        return wasMade
    }

    boolean finished(int playerNumber) {
        return tunsMadeAfterDeckEmpty > playerNumber
    }
}
