package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class HanabiPlayer implements Serializable{

    private String name
    private List<HanabiCard> cardsOnHand
}
