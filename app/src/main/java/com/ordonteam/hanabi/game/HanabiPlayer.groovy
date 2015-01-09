package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.view.CardView
import groovy.transform.CompileStatic

@CompileStatic
class HanabiPlayer implements Serializable{
    static final long serialVersionUID = 42L;
    static Map<Integer, IntRange> maxCardsFor = [(2): (1..5), (3): (1..5), (4): (1..4), (5): (1..4)]

    List<HanabiCard> cardsOnHand = []

    HanabiPlayer(HanabiGame game, int playersNumber) {
        cardsOnHand = maxCardsFor[playersNumber].collect {
            game.drawCard()
        }
    }

    CardColor hintColor(int cardIndex) {
        CardColor color = cardsOnHand.get(cardIndex).color
        cardsOnHand.each { HanabiCard card ->
            if (card.color == color)
                card.isColorKnown = true
        }
        return color
    }

    CardValue hintNumber(int cardIndex) {
        CardValue value = cardsOnHand.get(cardIndex).value
        cardsOnHand.each { HanabiCard card ->
            if (card.value == value)
                card.isValueKnown = true
        }
        return value
    }

    HanabiCard rejectCard(int index, HanabiCard newCard) {
        HanabiCard rejected = cardsOnHand.remove(index)
        cardsOnHand.add(newCard)
        return rejected
    }

    void updateCards(List<CardView> row, boolean self) {
        for (int i = 0; i < cardsOnHand.size(); i++) {
            if (self)
                row[i].setUserCard(cardsOnHand[i])
            else
                row[i].setPlayerCard(cardsOnHand[i])
        }
    }
}
