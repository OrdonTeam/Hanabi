package com.ordonteam.hanabi.gms

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractGamesMatchActivity extends AbstractGamesActivity implements OnTurnBasedMatchUpdateReceivedListener{
    protected TurnBasedMatch match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    protected void leaveMatch() {
        if (isMyTurn()) {
            Games.TurnBasedMultiplayer.leaveMatchDuringTurn(client, match.getMatchId(), nextPlayerId())
        } else {
            Games.TurnBasedMultiplayer.leaveMatch(client, match.getMatchId())
        }
    }

    protected boolean isMyTurn() {
        return match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN
    }

    protected int getPlayersNumber() {
        match.getParticipantIds().size() + match.availableAutoMatchSlots
    }

    protected int myIndexOnGmsList() {
        String playerId = Games.Players.getCurrentPlayerId(client);
        String myParticipantId = match.getParticipantId(playerId);
        List<String> participantIds = match.getParticipantIds();

        return participantIds.indexOf(participantIds.find {
            it == myParticipantId
        })
    }

    protected String nextPlayerId() {
        String playerId = Games.Players.getCurrentPlayerId(client);
        String myParticipantId = match.getParticipantId(playerId);
        return nextPlayerId(myParticipantId)
    }

    protected String nextPlayerId(String myParticipantId) {
        int desiredIndex = -1;
        List<String> participantIds = match.getParticipantIds();
        participantIds.size().times {
            if (participantIds.get(it) == myParticipantId) {
                desiredIndex = it + 1;
            }
        }
        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        } else if (this.match.getAvailableAutoMatchSlots() <= 0) {
            return participantIds.get(0);
        } else {
            return null;
        }
    }

    @Override
    void onTurnBasedMatchRemoved(String s) {
        Log.e("onTurnBasedMatchRemoved", "onTurnBasedMatchRemoved")
    }
}
