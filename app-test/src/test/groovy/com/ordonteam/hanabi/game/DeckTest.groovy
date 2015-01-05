package com.ordonteam.hanabi.game

import spock.lang.Specification

class DeckTest extends Specification {

    def "Should remove random element"() {
        given:
        Deck deck = new Deck()

        when:
        deck.drawCard()

        then:
        deck.cards.size() == 49
    }

}