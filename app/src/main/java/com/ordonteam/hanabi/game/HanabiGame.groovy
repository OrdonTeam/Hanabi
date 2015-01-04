package com.ordonteam.hanabi.game

import android.util.Log
import com.ordonteam.hanabi.utils.Utils
import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.CardsRow
import groovy.transform.CompileStatic

@CompileStatic
class HanabiGame implements Serializable {

    static Map<Integer, Integer> numberOfCardsForPlayerNumber = [(2): 5, (3): 5, (4): 4, (5): 4]

    int tipsNumber = 8
    int thundersNumber = 3
    PlayedCards playedCards = new PlayedCards()
    List<HanabiCard> rejectedCards = new ArrayList<>() //TODO: rejectCard()
    List<HanabiCard> availableCards = new ArrayList<>()
    List<HanabiPlayer> players = new ArrayList<>() //TODO: consider add gms id to player, and match players using them

    HanabiGame(int playersNumber) {

        CardColor.values().each { CardColor color ->
            CardValue.values().each { CardValue value ->
                value.getMax().times {
                    availableCards.add(new HanabiCard(color, value))
                }
            }
        }
        players = dealCards(playersNumber)
    }

    private List<HanabiPlayer> dealCards(int playersNumber) {

        List<HanabiPlayer> players = new ArrayList<>()
        playersNumber.times {
            List<HanabiCard> cardsOnHand = new ArrayList<>()
            int numberOfCards = numberOfCardsForPlayerNumber[playersNumber]
            numberOfCards.times {
                cardsOnHand.add(getCardFromStack())
            }
            players.add(new HanabiPlayer(cardsOnHand))
        }
        return players
    }

    private int numberOfCardsForPlayerNumber(int playerNumber) {
        playerNumber == 2 || playerNumber == 3 ? 5 : 4
    }

    HanabiCard getCardFromStack() {
        return Utils.removeRandom(availableCards)
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


    boolean isGameFinished() {
        return false
    }

    void makeThunder() {
        thundersNumber--
    }

    boolean isLowerCardWithTheSameColorOnTable(HanabiCard theCard) {
        return playedCards.isLowerCardWithTheSameColorOnTable(theCard)
    }

    void updateCards(List<CardsRow> cardsRow, int playerId) {
        Log.i("czy", "sie wywoluje")
        players.eachWithIndex { HanabiPlayer player, int playerIndex ->
            int rowIndex = getSpecialIndex(playerId, playerIndex)
            CardsRow row = cardsRow.get(rowIndex)
            player.cardsOnHand.eachWithIndex { HanabiCard card, int i ->
                CardView cardView = row.cardViewList.get(i)
                if(playerId==playerIndex)
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

    void addPlayerCard(HanabiCard card){
        playedCards.add(card)
    }

    HanabiPlayer getPlayerAt(int index){
        return players.get(index)
    }
}