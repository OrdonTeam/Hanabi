package com.ordonteam.hanabi.game

import android.util.Log
import com.ordonteam.hanabi.utils.Utils
import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.CardsRow
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
class HanabiGame implements Serializable{

    int tipsNumber = 8
    int thundersNumber = 3
    List<HanabiCard> playedCards = new ArrayList<>()
    List<HanabiCard> rejectedCards = new ArrayList<>() //TODO: rejectCard()
    List<HanabiCard> availableCards = new ArrayList<>()
    List<HanabiPlayer> players = new ArrayList<>()

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
            int numberOfCards = numberOfCardsForPlayerNumber(playersNumber)
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

    @CompileDynamic
    int getMaxPlayedColorValue(CardColor cardColor) {
        return playedCards.findAll { HanabiCard card ->
            card.color == cardColor
        }?.collect {HanabiCard card ->
            card.value.value
        }?.max()?:0
    }

    byte[] persist(){
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        byteOutputStream.withObjectOutputStream {ObjectOutputStream stream ->
            stream.writeObject(this)
        }
        return byteOutputStream.toByteArray()
    }

    static HanabiGame unpersist(byte[] bytes) {
        HanabiGame restoredGame
        new ByteArrayInputStream(bytes).withObjectInputStream {ObjectInputStream stream ->
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

    boolean isLowerCardWithTheSameColorOnTable(HanabiCard theCard){
        return playedCards.findAll {
            theCard.color == it.color && theCard.value.value -1 == it.value.value
        }.size() == 1
    }

    void metod(List<CardsRow> cardsRow, int playerId) {
        Log.i("czy","sie wywoluje")
        players.eachWithIndex { HanabiPlayer player, int playerIndex ->
            int rowIndex = getSpecialIndex(playerId,playerIndex)
            CardsRow row = cardsRow.get(rowIndex)
            player.cardsOnHand.eachWithIndex { HanabiCard card, int i ->
                CardView cardView = row.cardViewList.get(i)
                cardView.setColor(card.color.color)
                cardView.setNumber("$card.value.value")
            }
        }
    }

    int getSpecialIndex(int playerId, int playerIndex) {
        if(playerId == playerIndex)
            return 4;
        else{
            return (playerIndex-playerId+players.size()-1)%players.size()
        }
    }
}