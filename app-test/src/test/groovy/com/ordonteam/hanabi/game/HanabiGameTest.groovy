package com.ordonteam.hanabi.game

import spock.lang.Specification

class HanabiGameTest extends Specification {

    def "Test HanabiGame Constructor"() {
        int playersNumber = 5
        when:
        HanabiGame game = new HanabiGame(playersNumber);

        then:
        game != null
    }

    def "Should persists and unpersist hanabiGame object"() {
        int playersNumber = 5
        given:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)
        hanabiGame.availableCards = [new HanabiCard()]

        when:
        hanabiGame = HanabiGame.unpersist(hanabiGame.persist())

        then:
        hanabiGame.availableCards.size() == 1
    }

    def "Should remove random element"() {
        int playersNumber = 5
        given:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)

        when:
        hanabiGame.getCardFromStack()

        then:
        hanabiGame.availableCards.size() == 29 // 50 - 5*4 - 1
    }

    def "Should create 5 players and deal 4 cards each"() {
        int playersNumber = 5
        when:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)

        then:
        hanabiGame.availableCards.size() == 30 // 50 - 5*4
        hanabiGame.players.get(0).cardsOnHand.size() == 4
    }

    def "Should create 2 players and deal 5 cards each"() {
        int playersNumber = 2
        when:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)

        then:
        hanabiGame.availableCards.size() == 40 // 50 - 2*5
        hanabiGame.players.get(0).cardsOnHand.size() == 5
    }

//    def "Should throw exception for mora than 5 players"() {
//        int playersNumber = 6
//        when:
//        HanabiGame hanabiGame = new HanabiGame(playersNumber)
//
//        then:
//        hanabiGame.
//    }
}