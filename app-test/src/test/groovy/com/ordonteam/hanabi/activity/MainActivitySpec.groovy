package com.ordonteam.hanabi.activity

import org.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification

class MainActivitySpec extends RoboSpecification {
    def "Inject Test"() {
        given:
        MainActivity activity = Robolectric.buildActivity(MainActivity.class).create().get()

        expect:
        activity.buttonPlay != null
    }
}