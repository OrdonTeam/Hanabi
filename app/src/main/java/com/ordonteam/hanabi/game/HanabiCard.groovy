package com.ordonteam.hanabi.game

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class HanabiCard {

    final CardColor color
    final CardValue value
}
