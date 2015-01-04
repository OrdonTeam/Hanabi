package com.ordonteam.hanabi.game

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import static com.ordonteam.hanabi.game.CardValue.ZERO

@CompileStatic
class PlayedCards implements Serializable{
//    List<HanabiCard> cards = []
    Map<CardColor,CardValue> cards = new HashMap<>()

    PlayedCards() {
        CardColor.values().each {CardColor color ->
            cards.put(color, ZERO)
        }
    }

    void add(HanabiCard hanabiCard) {
        if(!isLowerCardWithTheSameColorOnTable(hanabiCard))
            throw new RuntimeException("Unplayable card!/nCurrent status: $cards/nPlayed card: $hanabiCard")
        cards.put(hanabiCard.color,hanabiCard.value)
    }

    int getMaxPlayedColorValue(CardColor cardColor) {
        return cards[cardColor].value
    }

    boolean isLowerCardWithTheSameColorOnTable(HanabiCard theCard) {
        return cards[theCard.color].value + 1 == theCard.value.value
    }
}
