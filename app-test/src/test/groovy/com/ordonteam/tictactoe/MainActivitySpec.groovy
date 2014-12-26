package com.ordonteam.tictactoe

import org.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification

class MainActivitySpec extends RoboSpecification {

    def "Robo Guice Test"() {
        given:
        def activity = Robolectric.buildActivity(EmptyActivity)
        def create = activity.create()

        expect:
        create.get() != null
    }
}