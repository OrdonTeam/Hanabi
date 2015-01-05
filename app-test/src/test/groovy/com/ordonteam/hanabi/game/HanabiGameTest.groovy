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
        hanabiGame.rejectedCards = [new HanabiCard(CardColor.RED,CardValue.ONE)]

        when:
        hanabiGame = HanabiGame.unpersist(hanabiGame.persist())

        then:
        hanabiGame.rejectedCards.size() == 1
    }

    def "Should create 5 players and deal 4 cards each"() {
        int playersNumber = 5
        when:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)

        then:
        hanabiGame.players.every{ HanabiPlayer player ->
            player.cardsOnHand.size() == 4
        }
    }

    def "Should create 2 players and deal 5 cards each"() {
        int playersNumber = 2
        when:
        HanabiGame hanabiGame = new HanabiGame(playersNumber)

        then:
        hanabiGame.players.every{ HanabiPlayer player ->
            player.cardsOnHand.size() == 5
        }
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