package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class Deck implements Serializable{
    List<HanabiCard> cards = []

    Deck() {
        CardColor.values().each { CardColor color ->
            CardValue.values().each { CardValue value ->
                value.getMax().times {
                    cards.add(new HanabiCard(color, value))
                }
            }
        }
    }

    HanabiCard drawCard() {
        Random rand = new Random()
        int index = rand.nextInt(cards.size())
        return cards.remove(index)
    }

    int size() {
        return cards.size()
    }
}
