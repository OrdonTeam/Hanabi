package com.ordonteam.hanabi.game

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

}
