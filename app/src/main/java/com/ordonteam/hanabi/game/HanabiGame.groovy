package com.ordonteam.hanabi.game

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

    HanabiGame() {
        CardColor.values().each { CardColor color ->
            CardValue.values().each { CardValue value ->
                value.getMax().times {
                    availableCards.add(new HanabiCard(color, value))
                }
            }
        }
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

    public static int randInt(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
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
}