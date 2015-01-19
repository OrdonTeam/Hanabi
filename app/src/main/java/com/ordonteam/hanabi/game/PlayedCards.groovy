package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import com.ordonteam.hanabi.view.common.CardView
import com.ordonteam.hanabi.view.row.CardsRow
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

import static com.ordonteam.hanabi.model.CardValue.FIVE
import static com.ordonteam.hanabi.model.CardValue.ZERO

@CompileStatic
class PlayedCards implements Serializable {
    static final long serialVersionUID = 42L;
    Map<CardColor, CardValue> cards = new HashMap<>()

    @CompileDynamic
    PlayedCards() {
        CardColor.colors().each { CardColor color ->
            cards.put(color, ZERO)
        }
    }

    void add(HanabiCard hanabiCard) {
        if (!isPlayable(hanabiCard))
            throw new RuntimeException("Unplayable card!/nCurrent status: $cards/nPlayed card: $hanabiCard")
        cards.put(hanabiCard.color, hanabiCard.value)
    }

    boolean isPlayable(HanabiCard theCard) {
        return cards[theCard.color].inNext(theCard.value)
    }

    void updatePlayedCards(CardsRow cardsRow) {
        cards.each { CardColor color, CardValue value ->
            CardView cardView = cardsRow.cardViewList.get(color.placeOnBoard)
            cardView.setCard(color, value)
        }
    }

    int score() {
        return (int) cards.values().sum { CardValue cardValue ->
            Integer.valueOf(cardValue.value)
        }
    }

    boolean areAll() {
        return cards.values().every { CardValue cardValue ->
            cardValue == FIVE
        }
    }
}
