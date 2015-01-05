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

    void hintColor(CardColor cardColor) {
        cardsOnHand.each { HanabiCard card ->
            if (card.color == cardColor )
                card.isColorKnown = true
        }
    }

    void hintNumber(CardValue cardValue) {
        cardsOnHand.each { HanabiCard card ->
            if (card.value == cardValue )
                card.isValueKnown = true
        }
    }

    CardColor getColorOf(int index) {
        return cardsOnHand.get(index-1).color
    }

    HanabiCard removeCardAt(int index) {
        return cardsOnHand.remove(index-1)
    }

    CardValue getValueOf(int index) {
        return cardsOnHand.get(index-1).value
    }

    void getCardFromStack(HanabiGame game) {
        cardsOnHand.add(game.drawCard())
    }
}
