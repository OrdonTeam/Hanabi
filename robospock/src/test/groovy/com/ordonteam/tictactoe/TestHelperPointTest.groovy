package com.ordonteam.tictactoe

import spock.lang.Specification

class TestHelperPointTest extends Specification {
    def "Robospock should run with groovy 2.4"() {
        when:
        TestHelperPoint p1 = new TestHelperPoint(0, 0)
        TestHelperPoint p2 = new TestHelperPoint(0, 0)

        then:
        p1==p2
    }
}
