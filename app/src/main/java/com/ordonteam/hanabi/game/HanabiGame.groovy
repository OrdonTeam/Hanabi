package com.ordonteam.hanabi.game

import android.util.Log
import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.CardsRow
import com.ordonteam.hanabi.view.GameInfoView
import groovy.transform.CompileStatic

import static com.ordonteam.hanabi.game.CardValue.FIVE

@CompileStatic
class HanabiGame implements Serializable {

    Tips tips = new Tips()
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
        Log.e("updateCards","playerId=$playerId")
        List<CardsRow> cut = cardsRow.take(players.size())
        List<CardsRow> list = (cut + cut).drop(players.size() - playerId).take(players.size())
        for (int i = 0; i < players.size(); i++) {
            players[i].updateCards(list[i].cardViewList, (i == playerId))
        }
    }

    void updatePlayedCards(CardsRow cardsRow) {
        playedCards.updatePlayedCards(cardsRow)
    }

    boolean hintPlayerColor(int playerIndex, int indexCardNumber) {
        return tips.useTip {
            HanabiPlayer player = players.get(playerIndex)
            player.hintColor(indexCardNumber)
        }
    }

    boolean hintPlayerNumber(int playerIndex, int indexCardNumber) {
        return tips.useTip {
            HanabiPlayer player = players.get(playerIndex)
            player.hintNumber(indexCardNumber)
        }
    }

    boolean rejectPlayerCard(int playerIndex, int indexCardNumber) {
        HanabiPlayer player = players.get(playerIndex)
        rejectedCards.add(player.rejectCard(indexCardNumber, drawCard()))
        tips.add()
        return true
    }

    boolean playPlayerCard(int playerIndex, int indexCardNumber) {
        HanabiPlayer player = players.get(playerIndex)
        HanabiCard playedCard = player.rejectCard(indexCardNumber, drawCard())

        if (playedCards.isPlayable(playedCard)) {
            playedCards.add(playedCard)
            if (playedCard.value == FIVE) {
                tips.add()
            }
        } else {
            rejectedCards.add(playedCard)
            thundersNumber--
        }
        return true
    }

    void updateGameInfo(GameInfoView gameInfoView) {
        gameInfoView.setTipsNumber(tips.get())
        gameInfoView.setThundersNumber(thundersNumber)
        if (!rejectedCards.empty)
            gameInfoView.setTopRejectedCard(rejectedCards.last())
    }

    boolean isGameFinished() {
        return false
    }
}