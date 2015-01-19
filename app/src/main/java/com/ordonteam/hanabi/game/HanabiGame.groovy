package com.ordonteam.hanabi.game

import android.util.Log
import android.widget.TextView
import com.google.android.gms.games.multiplayer.Participant
import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import com.ordonteam.hanabi.view.GameInfoView
import com.ordonteam.hanabi.view.row.CardsRow
import groovy.transform.CompileStatic

import static com.ordonteam.hanabi.model.CardValue.FIVE

@CompileStatic
class HanabiGame implements Serializable {
    static final long serialVersionUID = 42L;

    Tips tips = new Tips()
    int thundersNumber = 3
    PlayedCards playedCards = new PlayedCards()
    Deck deck = new Deck()
    List<HanabiCard> rejectedCards = new ArrayList<>()
    List<HanabiPlayer> players = new ArrayList<>()
    List<String> logs = []
    Turns turns = new Turns()
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
        Log.e("updateCards", "playerId=$playerId")
        List<CardsRow> cut = cardsRow.take(players.size())
        List<CardsRow> list = (cut + cut).drop(players.size() - playerId).take(players.size())
        for (int i = 0; i < players.size(); i++) {
            players[i].updateCards(list[i].cardViewList, (i == playerId))
        }
    }

    void updatePlayedCards(CardsRow cardsRow) {
        playedCards.updatePlayedCards(cardsRow)
    }

    void updateGameInfo(GameInfoView gameInfoView) {
        gameInfoView.setTipsNumber(tips.get())
        gameInfoView.setThundersNumber(thundersNumber)
        gameInfoView.setRemainingCardsLeft(deck.size())
        if (!rejectedCards.empty)
            gameInfoView.setTopRejectedCard(rejectedCards.last())
    }

    void updateLogs(TextView textView, List<Participant> participants, int myPosition) {
        String logsString = logs.reverse().join('\n')
        for (int i = 0; i < participants.size(); i++) {
            String name = i == myPosition ? 'You' : participants[i].displayName
            logsString = logsString.replaceAll("$i", name)
        }
        textView.setText(logsString)
    }

    boolean hintPlayerColor(int playerIndex, int indexCardNumber, int currentPlayer) {
        return turns.takeTurn(deck) {
            tips.useTip {
                HanabiPlayer player = players.get(playerIndex)
                CardColor color = player.hintColor(indexCardNumber)
                logs.add("> $currentPlayer gave tip to $playerIndex. Color ${color.name()}".toString())
            }
        }
    }

    boolean hintPlayerNumber(int playerIndex, int indexCardNumber, int currentPlayer) {
        return turns.takeTurn(deck) {
            tips.useTip {
                HanabiPlayer player = players.get(playerIndex)
                CardValue value = player.hintNumber(indexCardNumber)
                logs.add("> $currentPlayer gave tip to $playerIndex. Value ${value.name()}".toString())
            }
        }
    }

    boolean rejectPlayerCard(int playerIndex, int indexCardNumber) {
        return turns.takeTurn(deck) {
            HanabiPlayer player = players.get(playerIndex)
            HanabiCard rejectedCard = player.rejectCard(indexCardNumber, drawCard())
            rejectedCards.add(rejectedCard)
            tips.add()
            logs.add("> $playerIndex rejected card. ${rejectedCard.value.name()} ${rejectedCard.color.name()}".toString())
            return true
        }
    }

    boolean playPlayerCard(int playerIndex, int indexCardNumber) {
        return turns.takeTurn(deck) {
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
            logs.add("> $playerIndex played card. ${playedCard.value.name()} ${playedCard.color.name()}".toString())
            return true
        }
    }

    boolean isGameFinished() {
        if (thundersNumber == 0)
            return true
        if (playedCards.areAll())
            return true
        if (turns.finished(players.size()))
            return true
        return false
    }

    int score() {
        return playedCards.score();
    }
}