package com.ordonteam.hanabi.game

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class HanabiCard implements Serializable {

    final CardColor color
    final CardValue value
}
