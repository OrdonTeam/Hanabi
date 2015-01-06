package com.ordonteam.hanabi.game

import groovy.transform.CompileStatic

@CompileStatic
class Tips implements Serializable {
    static final long serialVersionUID = 42L;

    int tips = 8

    void add() {
        if (tips <= 7) {
            tips++
        }
    }

    boolean useTip(Closure closure) {
        if (tips > 0) {
            tips--
            closure()
            return true;
        }
        return false;
    }

    int get() {
        return tips
    }
}
