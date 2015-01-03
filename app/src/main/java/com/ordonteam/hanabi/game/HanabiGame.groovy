package com.ordonteam.hanabi.game

import com.ordonteam.hanabi.game.actions.HintPlayerColor
import com.ordonteam.hanabi.game.actions.HintPlayerNumber
import com.ordonteam.hanabi.game.actions.PutCardPlayerAction
import com.ordonteam.hanabi.game.actions.RejectPlayerAction
import com.ordonteam.hanabi.utils.Utils
import com.ordonteam.hanabi.game.actions.BasePlayerAction
import com.ordonteam.hanabi.utils.Utils
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

    List<HanabiPlayer> dealCards(int playersNumber) {

        List<HanabiPlayer> players = new ArrayList<>()
        for (int i = 0; i < playersNumber; i++) {

            List<HanabiCard> cardsOnHand
            if (playersNumber == 2 || playersNumber == 3) {

                cardsOnHand = new ArrayList<>()
                for (int j = 0; j < 5; j++) {
                    cardsOnHand.add(getCardFromStack())
                }
                players.add(new HanabiPlayer(cardsOnHand))
            }

            if (playersNumber == 4 || playersNumber == 5) {

                cardsOnHand = new ArrayList<>()
                for (int j = 0; j < 4; j++) {
                    cardsOnHand.add(getCardFromStack())
                }
                players.add(new HanabiPlayer(cardsOnHand))
            }
        }
        return players
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

    void makeAction(HintPlayerColor action) {
        HanabiPlayer destinationPlayer = players.get(action.destinationPlayer)
        CardColor color = destinationPlayer.getColorOf(action.indexCardColor)
        destinationPlayer.hintColor(color)
    }

    void makeAction(HintPlayerNumber action) {
        HanabiPlayer activePlayer = players.get(action.sourcePlayer)
    }

    boolean makeAction(PutCardPlayerAction action) {
        HanabiPlayer activePlayer = players.get(action.sourcePlayer)
        HanabiCard playedCard = activePlayer.cardsOnHand.get(action.card)
        playedCards.add(playedCard)
        activePlayer.cardsOnHand.remove(playedCard)
        activePlayer.cardsOnHand.add(getCardFromStack())

        if(!isLowerCardWithTheSameColorOnTable(playedCard)){
            makeThunder()
        }
        return isGameFinished()
    }

    boolean isGameFinished() {
        return false
    }

    private void makeThunder() {
        thundersNumber--
    }

    boolean makeAction(RejectPlayerAction action) {
        HanabiPlayer activePlayer = players.get(action.sourcePlayer)
        rejectedCards.add(activePlayer.cardsOnHand.get(action.card))
        activePlayer.cardsOnHand.add(getCardFromStack())
        if(tipsNumber <= 7){
            tipsNumber++
        }

        return isGameFinished()
    }

    boolean isLowerCardWithTheSameColorOnTable(HanabiCard theCard){
        return playedCards.findAll {
            theCard.color == it.color && theCard.value.value -1 == it.value.value
        }.size() == 1
    }

}