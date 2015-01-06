package com.ordonteam.hanabi.activity

import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import org.robolectric.Robolectric
import pl.polidea.robospock.RoboSpecification

class GameActivitySpec extends RoboSpecification {
    def "nextPlayerId should return first player when only one player"() {
        given:
        GameActivity activity = new GameActivity()
        activity.match = Mock(TurnBasedMatch)

        when:
        activity.match.getParticipantIds() >> { ['123'] }
        activity.match.getAvailableAutoMatchSlots() >> { 0 }

        then:
        activity.nextPlayerId( '123') == '123'
    }

    def "nextPlayerId should return second player when there is two players and current turn was player one"() {
        given:
        GameActivity activity = new GameActivity()
        activity.match = Mock(TurnBasedMatch)

        when:
        activity.match.getParticipantIds() >> { ['1', '2'] }
        activity.match.getAvailableAutoMatchSlots() >> { 0 }

        then:
        activity.nextPlayerId( '1') == '2'
    }

    def "nextPlayerId should return first player when there is two players and current turn was player two"() {
        given:
        GameActivity activity = new GameActivity()
        activity.match = Mock(TurnBasedMatch)

        when:
        activity.match.getParticipantIds() >> { ['1', '2'] }
        activity.match.getAvailableAutoMatchSlots() >> { 0 }

        then:
        activity.nextPlayerId( '2') == '1'
    }

    def "nextPlayerId should return second player when there is two players and current turn was player one and there are available slots"() {
        given:
        GameActivity activity = new GameActivity()
        activity.match = Mock(TurnBasedMatch)

        when:
        activity.match.getParticipantIds() >> { ['1', '2'] }
        activity.match.getAvailableAutoMatchSlots() >> { 1 }

        then:
        activity.nextPlayerId( '1') == '2'
    }

    def "nextPlayerId should return null when there is two players and current turn was player two and there are available slots"() {
        given:
        GameActivity activity = new GameActivity()
        activity.match = Mock(TurnBasedMatch)

        when:
        activity.match.getParticipantIds() >> { ['1', '2'] }
        activity.match.getAvailableAutoMatchSlots() >> { 1 }

        then:
        activity.nextPlayerId('2') == null
    }
}