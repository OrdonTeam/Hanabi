package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class HanabiGame{

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

    public static int randInt(int max) {
        Random rand = new Random();
        return rand.nextInt(max);
    }
}