package com.ordonteam.hanabi.gms

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.google.android.gms.common.api.Result
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractGamesMatchActivity extends AbstractGamesActivity
        implements OnTurnBasedMatchUpdateReceivedListener{
    protected TurnBasedMatch match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void acceptInvitation(String invitationId) {
        Games.TurnBasedMultiplayer.acceptInvitation(client, invitationId).setResultCallback(this.&onInitiateMatchResult)
    }

    void createMatch(TurnBasedMatchConfig config) {
        Games.TurnBasedMultiplayer.createMatch(client, config).setResultCallback(this.&onInitiateMatchResult)
    }

    void onInitiateMatchResult(TurnBasedMultiplayer.InitiateMatchResult result) {
        initMatch(result.match)
    }
    void loadMatch(String matchId) {
        Games.TurnBasedMultiplayer.loadMatch(client,matchId).setResultCallback(this.&onLoadMatchResult)
    }
    void onLoadMatchResult(TurnBasedMultiplayer.LoadMatchResult result) {
        onResult(result,result.match)
    }
    void onResult(Result result, TurnBasedMatch match) {
        if(result.getStatus().success)
            initMatch(match)
    }

    abstract void initMatch(TurnBasedMatch match);

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
