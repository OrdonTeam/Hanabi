package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class Deck implements Serializable {
    static final long serialVersionUID = 42L;

    List<HanabiCard> cards = []

    @CompileDynamic
    Deck() {
        CardColor.colors().each { CardColor color ->
            CardValue.values().each { CardValue value ->
                value.amount().times {
                    cards.add(new HanabiCard(color, value))
                }
            }
        }
    }

    HanabiCard drawCard() {
        if(!cards.empty){
            Random rand = new Random()
            int index = rand.nextInt(cards.size())
            return cards.remove(index)
        } else {
            return HanabiCard.emptyCard()
        }
    }

    int size() {
        return cards.size()
    }

    boolean isEmpty() {
        return cards.empty
    }
}
