package com.ordonteam.hanabi.game

import spock.lang.Specification

class HanabiGameTest extends Specification {
    def "Test HanabiGameTest"() {
        when:
        HanabiGame game = new HanabiGame();

        then:
        game != null
        game.availableCards.size() == 50
    }
}