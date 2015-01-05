package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class HanabiPlayer implements Serializable{
    static Map<Integer, IntRange> maxCardsFor = [(2): (1..5), (3): (1..5), (4): (1..4), (5): (1..4)]

    private String name
    List<HanabiCard> cardsOnHand = []

    HanabiPlayer(HanabiGame game,int playersNumber){
        cardsOnHand = maxCardsFor[playersNumber].collect{
            game.drawCard()
        }
    }

    void hintColor(int cardIndex) {
        cardsOnHand.each { HanabiCard card ->
            if (card.color == getColorOf(cardIndex) )
                card.isColorKnown = true
        }
    }

    void hintNumber(int cardIndex) {
        cardsOnHand.each { HanabiCard card ->
            if (card.value == getValueOf(cardIndex) )
                card.isValueKnown = true
        }
    }

    CardColor getColorOf(int index) {
        return cardsOnHand.get(index).color
    }

    HanabiCard rejectCard(int index) {
        return cardsOnHand.remove(index)
    }

    CardValue getValueOf(int index) {
        return cardsOnHand.get(index).value
    }

    void drawCard(HanabiCard card) {
        cardsOnHand.add(card)
    }
}
