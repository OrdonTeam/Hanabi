package com.ordonteam.hanabi.game

import android.util.Log
import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.CardsRow
import groovy.transform.CompileStatic

@CompileStatic
class HanabiGame implements Serializable {

    int tipsNumber = 8
    int thundersNumber = 3
    PlayedCards playedCards = new PlayedCards()
    Deck deck = new Deck()
    List<HanabiCard> rejectedCards = new ArrayList<>() //TODO: rejectCard()
    List<HanabiPlayer> players = new ArrayList<>()
    //TODO: consider add gms id to player, and match players using them

    HanabiGame(int playersNumber) {
        players = (1..playersNumber).collect {
            new HanabiPlayer(this, playersNumber)
        }
    }

    HanabiCard drawCard() {
        return deck.drawCard()
    }

    byte[] persist() {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byteOutputStream.withObjectOutputStream { ObjectOutputStream stream ->
            stream.writeObject(this)
        }
        return byteOutputStream.toByteArray()
    }

    static HanabiGame unpersist(byte[] bytes) {
        HanabiGame restoredGame
        new ByteArrayInputStream(bytes).withObjectInputStream { ObjectInputStream stream ->
            restoredGame = stream.readObject() as HanabiGame
        }
        return restoredGame
    }

    void updateCards(List<CardsRow> cardsRow, int playerId) {
        Log.i("czy", "sie wywoluje")
        players.eachWithIndex { HanabiPlayer player, int playerIndex ->
            int rowIndex = getSpecialIndex(playerId, playerIndex)
            CardsRow row = cardsRow.get(rowIndex)
            player.cardsOnHand.eachWithIndex { HanabiCard card, int i ->
                CardView cardView = row.cardViewList.get(i)
                if (playerId == playerIndex)
                    cardView.setUserCard(card)
                else
                    cardView.setCard(card)
            }
        }
    }

    int getSpecialIndex(int playerId, int playerIndex) {
        if (playerId == playerIndex)
            return 4;
        else {
            return (playerIndex - playerId + players.size() - 1) % players.size()
        }
    }

    void updatePlayedCards(CardsRow cardsRow) {
        playedCards.updatePlayedCards(cardsRow)
    }

    boolean hintPlayerColor(int playerIndex, int indexCardNumber) {
        if (tipsNumber > 0) {
            HanabiPlayer player = players.get(playerIndex)
            player.hintColor(indexCardNumber)
            tipsNumber--
            return true
        }
        return false
    }

    boolean hintPlayerNumber(int playerIndex, int indexCardNumber) {
        if (tipsNumber > 0) {
            HanabiPlayer player = players.get(playerIndex)
            player.hintNumber(indexCardNumber)
            tipsNumber--
            return true
        }
        return false
    }

    boolean rejectPlayerCard(int playerIndex, int indexCardNumber) {
        HanabiPlayer player = players.get(playerIndex)
        rejectedCards.add(player.rejectCard(indexCardNumber, drawCard()))
        if (tipsNumber <= 7) {
            tipsNumber++
        }
        return true
    }

    boolean playPlayerCard(int playerIndex, int indexCardNumber) {
        HanabiPlayer player = players.get(playerIndex)
        HanabiCard playedCard = player.rejectCard(indexCardNumber, drawCard())

        if (playedCards.isPlayable(playedCard)) {
            playedCards.add(playedCard)
        } else {
            rejectedCards.add(playedCard)
            thundersNumber--
        }
        return true
    }

    boolean isGameFinished() {
        return false
    }
}