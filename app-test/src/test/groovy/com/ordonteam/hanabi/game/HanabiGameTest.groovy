package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import spock.lang.Ignore
import spock.lang.Specification

import static com.ordonteam.hanabi.model.CardColor.RED
import static com.ordonteam.hanabi.model.CardValue.FIVE
import static com.ordonteam.hanabi.model.CardValue.ONE
import static com.ordonteam.hanabi.model.CardValue.TWO

class HanabiGameTest extends Specification {

    def "Should persists and unpersist hanabiGame object"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(5)
        hanabiGame.rejectedCards = [new HanabiCard(RED,ONE)]

        when:
        hanabiGame = HanabiGame.unpersist(hanabiGame.persist())

        then:
        hanabiGame.rejectedCards.size() == 1
    }

    def "Should create 5 players and deal 4 cards each"() {
        when:
        HanabiGame hanabiGame = new HanabiGame(5)

        then:
        hanabiGame.players.every{ HanabiPlayer player ->
            player.cardsOnHand.size() == 4
        }
    }

    def "Should create 2 players and deal 5 cards each"() {
        when:
        HanabiGame hanabiGame = new HanabiGame(2)

        then:
        hanabiGame.players.every{ HanabiPlayer player ->
            player.cardsOnHand.size() == 5
        }
    }

    def "Test hintPlayerColor should show proper color"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        HanabiPlayer mock = Mock(HanabiPlayer)
        hanabiGame.players[0] = mock

        when:
        boolean success = hanabiGame.hintPlayerColor(0, 0, 0)

        then:
        success
        1 * mock.hintColor(0) >> CardColor.BLUE
    }

    def "Test hintPlayerColor should do nothig when not enough tips"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        hanabiGame.players[0] = Mock(HanabiPlayer)
        hanabiGame.tips.tips = 0

        when:
        boolean success = hanabiGame.hintPlayerColor(0, 0, 0)

        then:
        !success
        0 * hanabiGame.players[0].hintColor(0)
    }

    def "Test hintPlayerNumber should show proper number"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        HanabiPlayer mock = Mock(HanabiPlayer)
        hanabiGame.players[0] = mock

        when:
        boolean success = hanabiGame.hintPlayerNumber(0, 0, 0)

        then:
        success
        1 * mock.hintNumber(0) >> CardValue.FIVE
    }

    def "Test hintPlayerNumber should do nothig when not enough tips"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        hanabiGame.players[0] = Mock(HanabiPlayer)
        hanabiGame.tips.tips = 0

        when:
        boolean success = hanabiGame.hintPlayerNumber(0, 0, 0)

        then:
        !success
        0 * hanabiGame.players[0].hintNumber(0)
    }

    def "Test rejectPlayerCard should increase tip number"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        hanabiGame.tips.tips = 0

        when:
        boolean success = hanabiGame.rejectPlayerCard(0, 0)

        then:
        success
        1 == hanabiGame.tips.tips
    }

    def "Test rejectPlayerCard should NOT increase tip number when 8 of them"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        hanabiGame.tips.tips = 8

        when:
        boolean success = hanabiGame.rejectPlayerCard(0, 0)

        then:
        success
        8 == hanabiGame.tips.tips
    }

    def "Test rejectPlayerCard should increase rejected card number"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)

        when:
        boolean success = hanabiGame.rejectPlayerCard(0, 0)

        then:
        success
        !hanabiGame.rejectedCards.empty
    }

    def "Test rejectPlayerCard should ask player to change card from deck"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        HanabiCard randomCard = new HanabiCard(RED, FIVE)
        hanabiGame.deck = Mock(Deck)
        hanabiGame.players[0] = Mock(HanabiPlayer)

        when:
        hanabiGame.deck.drawCard() >> randomCard
        boolean success = hanabiGame.rejectPlayerCard(0, 0)

        then:
        success
        1 * hanabiGame.players[0].rejectCard(0,randomCard) >> randomCard
    }

    def "Test playPlayerCard should play"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        HanabiCard randomCard = new HanabiCard(RED, ONE)
        hanabiGame.deck = Mock(Deck)
        hanabiGame.players[0] = Mock(HanabiPlayer)

        when:
        hanabiGame.deck.drawCard() >> randomCard
        boolean success = hanabiGame.playPlayerCard(0, 0)

        then:
        success
        1 * hanabiGame.players[0].rejectCard(0,randomCard) >> randomCard
        !hanabiGame.playedCards.cards.empty
        hanabiGame.rejectedCards.empty
    }

    def "Test playPlayerCard should make tunder"() {
        given:
        HanabiGame hanabiGame = new HanabiGame(2)
        HanabiCard randomCard = new HanabiCard(RED, TWO)
        hanabiGame.deck = Mock(Deck)
        hanabiGame.players[0] = Mock(HanabiPlayer)
        hanabiGame.playedCards = Mock(PlayedCards)

        when:
        hanabiGame.playedCards.isPlayable(_) >> false
//        hanabiGame.deck.drawCard() >> randomCard
        boolean success = hanabiGame.playPlayerCard(0, 0)

        then:
        success
        1 * hanabiGame.players[0].rejectCard(0,_) >> randomCard
        0 * hanabiGame.playedCards.add(_)
        !hanabiGame.rejectedCards.empty
    }
}