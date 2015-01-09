package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.CardsRow
import groovy.transform.CompileStatic

import static com.ordonteam.hanabi.game.CardValue.ZERO

@CompileStatic
class PlayedCards implements Serializable{
    static final long serialVersionUID = 42L;
    Map<CardColor,CardValue> cards = new HashMap<>()

    PlayedCards() {
        CardColor.values().each {CardColor color ->
            cards.put(color, ZERO)
        }
    }

    void add(HanabiCard hanabiCard) {
        if(!isPlayable(hanabiCard))
            throw new RuntimeException("Unplayable card!/nCurrent status: $cards/nPlayed card: $hanabiCard")
        cards.put(hanabiCard.color,hanabiCard.value)
    }

    int getMaxPlayedColorValue(CardColor cardColor) {
        return cards[cardColor].value
    }

    boolean isPlayable(HanabiCard theCard) {
        return cards[theCard.color].value + 1 == theCard.value.value
    }

    void updatePlayedCards(CardsRow cardsRow) {
        cards.each {CardColor color, CardValue value ->
            CardView cardView = cardsRow.cardViewList.get(color.placeOnBoard)
            cardView.setCard(color, value)
        }
    }
}
