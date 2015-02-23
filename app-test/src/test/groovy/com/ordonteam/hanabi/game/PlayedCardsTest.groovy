package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import spock.lang.Specification

class PlayedCardsTest extends Specification {

    def "Test getMaxPlayedColorValue when empty"() {
        when:
        PlayedCards game = new PlayedCards();

        then:
        game.cards[CardColor.RED] == CardValue.ZERO
    }

    def "Test getMaxPlayedColorValue when no cards in matching color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.BLUE, CardValue.ONE))

        then:
        cards.cards[CardColor.RED] == CardValue.ZERO
    }

    def "Test getMaxPlayedColorValue when only one card in color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))

        then:
        cards.cards[CardColor.RED] == CardValue.ONE
    }

    def "Test getMaxPlayedColorValue when more than one card in color"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.TWO))

        then:
        cards.cards[CardColor.RED] == CardValue.TWO
    }

    def "Should calculate score"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.TWO))

        then:
        cards.score() == 2
    }

    def "Should calculate score in all colors"() {
        when:
        PlayedCards cards = new PlayedCards();
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.TWO))
        cards.add(new HanabiCard(CardColor.BLUE, CardValue.ONE))
        cards.add(new HanabiCard(CardColor.BLUE, CardValue.TWO))

        then:
        cards.score() == 4
    }

    def "Should are all return false"() {
        given:
        PlayedCards cards = new PlayedCards();

        when:
        cards.add(new HanabiCard(CardColor.RED, CardValue.ONE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.TWO))
        cards.add(new HanabiCard(CardColor.RED, CardValue.THREE))
        cards.add(new HanabiCard(CardColor.RED, CardValue.FOUR))
        cards.add(new HanabiCard(CardColor.RED, CardValue.FIVE))

        then:
        !cards.areAll()
    }

    def "Should are all return true"() {
        given:
        PlayedCards cards = new PlayedCards();

        when:
        CardColor.colors().each { CardColor color ->
            CardValue.values().findAll{ CardValue value ->
                value.amount() > 0
            }.each { CardValue value ->
                cards.add(new HanabiCard(color,value))
            }
        }

        then:
        cards.areAll()
    }
}