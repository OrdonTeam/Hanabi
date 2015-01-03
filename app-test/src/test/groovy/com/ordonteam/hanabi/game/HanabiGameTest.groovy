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

    def "Test getMaxPlayedColorValue when empty"() {
        when:
        HanabiGame game = new HanabiGame();

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 0

    }

    def "Test getMaxPlayedColorValue when no cards in matching color"() {
        when:
        HanabiGame game = new HanabiGame();
        game.playedCards = [new HanabiCard(CardColor.BLUE, CardValue.FIVE)]

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 0
    }

    def "Test getMaxPlayedColorValue when only one card in color"() {
        when:
        HanabiGame game = new HanabiGame();
        game.playedCards = [new HanabiCard(CardColor.RED, CardValue.FIVE)]

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 5
    }

    def "Test getMaxPlayedColorValue when more than one card in color"() {
        when:
        HanabiGame game = new HanabiGame();
        game.playedCards = [new HanabiCard(CardColor.RED, CardValue.FIVE), new HanabiCard(CardColor.RED, CardValue.ONE)]

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 5
    }

    def "Should persists and unpersist hanabiGame object"() {
        given:
        HanabiGame hanabiGame = new HanabiGame()
        hanabiGame.availableCards = [new HanabiCard()]

        when:
        hanabiGame = HanabiGame.unpersist(hanabiGame.persist())

        then:
        hanabiGame.availableCards.size() == 1
    }

    def "Should remove random element"() {
        given:
        HanabiGame hanabiGame = new HanabiGame()

        when:
        hanabiGame.getCardFromStack()

        then:
        hanabiGame.availableCards.size() == 49
    }
}