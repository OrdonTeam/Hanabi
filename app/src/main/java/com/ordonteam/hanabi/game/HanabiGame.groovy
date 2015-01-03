package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class HanabiGame implements Serializable{

    private int tipsNumber = 10
    private int thundersNumber = 3
    private List<HanabiCard> playedCards = new ArrayList<>() //TODO: getMaxColorValue()
    private List<HanabiCard> rejectedCards = new ArrayList<>() //TODO: rejectCard()
    List<HanabiCard> availableCards = new ArrayList<>()
    private List<HanabiPlayer> players

    HanabiGame() {

        CardColor.values().each {CardColor color->
            CardValue.values().each {CardValue value->
                value.getMax().times {
                    availableCards.add(new HanabiCard(color, value))
                }
            }
        }
    }

    HanabiCard getCardFromStack() {
        return availableCards.remove(randInt(availableCards.size()))
    }

    int getMaxColorValue(CardColor cardColor) {

        List<HanabiCard> inColor = playedCards.findAll {
            it.color == cardColor
        }
        HanabiCard max = inColor.max { HanabiCard card ->
            card.value.value
        }
        return max.value.value
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