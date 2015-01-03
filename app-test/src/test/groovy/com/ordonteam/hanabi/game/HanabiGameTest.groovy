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

    def "Should persists and unpersist hanabiGame object"() {
        given:
        HanabiGame hanabiGame = new HanabiGame()
        hanabiGame.availableCards = []
        hanabiGame.availableCards.add(new HanabiCard())

        when:
        hanabiGame = HanabiGame.unpersist(hanabiGame.persist())

        then:
            hanabiGame.availableCards.size() == 1
    }
}