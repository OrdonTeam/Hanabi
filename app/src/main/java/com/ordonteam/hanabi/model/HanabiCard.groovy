package com.ordonteam.hanabi.model

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class HanabiCard implements Serializable {
    static final long serialVersionUID = 42L;

    final CardColor color
    final CardValue value

    boolean isColorKnown = false
    boolean isValueKnown = false

    static HanabiCard emptyCard() {
        return new HanabiCard(CardColor.EMPTY,CardValue.EMPTY,true,true)
    }
}
