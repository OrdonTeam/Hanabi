package com.ordonteam.hanabi.game.actions

import com.ordonteam.hanabi.game.HanabiGame
import groovy.transform.CompileStatic


@CompileStatic
abstract class BasePlayerAction {

    Integer sourcePlayer

    abstract boolean doAction(HanabiGame game)
}
