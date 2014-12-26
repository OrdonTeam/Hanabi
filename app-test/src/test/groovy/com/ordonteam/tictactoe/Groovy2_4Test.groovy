package com.ordonteam.tictactoe

import groovy.transform.Canonical
import spock.lang.Specification

class Groovy2_4Test extends Specification {
    def "Robospock should run with groovy 2.4"() {
        when:
        PointHelper p1 = new PointHelper(0, 0)
        PointHelper p2 = new PointHelper(0, 0)

        then:
        p1==p2
    }

    @Canonical
    private class PointHelper {
        int x;
        int y;
    }
}
