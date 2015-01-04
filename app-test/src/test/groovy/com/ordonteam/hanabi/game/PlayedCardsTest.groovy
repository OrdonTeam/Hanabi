package com.ordonteam.hanabi.game

import spock.lang.Specification

class PlayedCardsTest extends Specification {

    def "Test getMaxPlayedColorValue when empty"() {
        when:
        PlayedCards game = new PlayedCards();

        then:
        game.getMaxPlayedColorValue(CardColor.RED) == 0
    }

    def "Test getMaxPlayedColorValue when no cards in matching color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.BLUE, CardValue.FIVE))

        then:
        cards.getMaxPlayedColorValue(CardColor.RED) == 0
    }

    def "Test getMaxPlayedColorValue when only one card in color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.FIVE))

        then:
        cards.getMaxPlayedColorValue(CardColor.RED) == 5
    }

    def "Test getMaxPlayedColorValue when more than one card in color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.FIVE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))

        then:
        cards.getMaxPlayedColorValue(CardColor.RED) == 5
    }

}