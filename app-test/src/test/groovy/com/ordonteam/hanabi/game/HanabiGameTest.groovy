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

    def "Test getMaxPlayedColorValue"() {
        when:
        HanabiGame game = new HanabiGame();

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 0

    }

    def "Test getMaxPlayedColorValue2"() {
        when:
        HanabiGame game = new HanabiGame();
        game.playedCards = [new HanabiCard(CardColor.BLUE, CardValue.FIVE)]

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 0

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