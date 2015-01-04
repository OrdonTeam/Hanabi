package com.ordonteam.hanabi.game

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class PlayedCards implements Serializable{
    List<HanabiCard> cards = []


    boolean isLowerCardWithTheSameColorOnTable(HanabiCard theCard) {
        return cards.findAll {
            theCard.color == it.color && theCard.value.value - 1 == it.value.value
        }.size() == 1
    }

    void add(HanabiCard hanabiCard) {
        cards.add(hanabiCard)
    }

    @CompileDynamic
    int getMaxPlayedColorValue(CardColor cardColor) {
        return cards.findAll { HanabiCard card ->
            card.color == cardColor
        }?.collect { HanabiCard card ->
            card.value.value
        }?.max() ?: 0
    }
}
