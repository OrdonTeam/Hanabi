package com.ordonteam.hanabi.game

import spock.lang.Specification

class HanabiGameTest extends Specification {
    def "HanabiGameTest"() {
        when:
        HanabiGame game = new HanabiGame();

        then:
        game != null
    }
}