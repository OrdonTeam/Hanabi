package com.ordonteam.hanabi.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.games.Games
import com.google.android.gms.games.Player
import com.google.android.gms.games.Players
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.hanabi.gms.AbstractGamesActivity
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.gms.GameConfig
import com.ordonteam.inject.InjectClickListener
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesActivity implements OnTurnBasedMatchUpdateReceivedListener {

    @InjectView(R.id.turn)
    Button turn;

    private TurnBasedMatchConfig config
    private String invId
    private TurnBasedMatch match

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        invId = intent.getStringExtra(Multiplayer.EXTRA_INVITATION)
        if (!invId)
            config = GameConfig.configFromIntent(intent)
    }

    @Override
    void onConnected(Bundle bundle) {
        if (invId) {
            Games.TurnBasedMultiplayer.acceptInvitation(client, invId).setResultCallback(this.&initiateMatchResult)
            Games.TurnBasedMultiplayer.registerMatchUpdateListener(client, this)
        } else {
            Games.TurnBasedMultiplayer.createMatch(client, config).setResultCallback(this.&initiateMatchResult)
            Games.TurnBasedMultiplayer.registerMatchUpdateListener(client, this)
        }
    }

    void initiateMatchResult(InitiateMatchResult result) {
        Log.e("status", "result=${result.status}")
        Log.e("status", "match=${result.match?.status}")
        Log.e("status", "turn=${result.match?.turnStatus}")
        String next = nextPlayerId(result.match)
        if (result.match?.data) {
            Log.e("status", "data=${new String(result.match.data)}")
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, 'second'.bytes, next)
                    .setResultCallback(this.&updateMatchResult)
        } else {
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, 'first'.bytes, next)
                    .setResultCallback(this.&updateMatchResult)
        }
        match = result.match
    }

    private String nextPlayerId(TurnBasedMatch match) {
        String playerId = Games.Players.getCurrentPlayerId(client);
        String myParticipantId = match.getParticipantId(playerId);

        return nextPlayerId(match, myParticipantId)
    }

    private String nextPlayerId(TurnBasedMatch match, String myParticipantId) {
        int desiredIndex = -1;

        ArrayList<String> participantIds = match.getParticipantIds();

        for (int i = 0; i < participantIds.size(); i++) {
            if (participantIds.get(i).equals(myParticipantId)) {
                desiredIndex = i + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            Log.e("status", "next=${participantIds.get(desiredIndex)}")
            return participantIds.get(desiredIndex);
        }

        if (match.getAvailableAutoMatchSlots() <= 0) {
            // You've run out of automatch slots, so we start over.
            Log.e("status", "next=${participantIds.get(0)}")
            return participantIds.get(0);
        } else {
            // You have not yet fully automatched, so null will find a new
            // person to play against.
            Log.e("status", "next=null")
            return null;
        }
    }

    void updateMatchResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        Log.e('updateMatchResult','update')
        Log.e("status", "result=${result.status}")
        Log.e("status", "match=${result.match?.status}")
        Log.e("status", "turn=${result.match?.turnStatus}")
        if (result.match?.data)
            Log.e("status", "data=${new String(result.match.data)}")
    }

    @InjectClickListener(R.id.log)
    void onLog(View view) {
        Games.Players.loadConnectedPlayers(client, true).setResultCallback({
            Players.LoadPlayersResult result ->
                result.players.each { Player player ->
                    Log.e("players", "player=${player.playerId}")
                }
        })
    }

    @Override
    void onTurnBasedMatchReceived(TurnBasedMatch match) {
        Log.e("status", "onTurnBasedMatchReceived")
        Log.e("status", "match=${match?.status}")
        Log.e("status", "turn=${match?.turnStatus}")
        if (match?.data)
            Log.e("status", "data=${new String(match.data)}")

        if( match?.status == TurnBasedMatch.MATCH_STATUS_CANCELED ){
            super.onBackPressed()
        }

        String next = nextPlayerId(match)
        turn.setOnClickListener({
            Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, 'next turn'.bytes, next).setResultCallback(this.&updateMatchResult)
            turn.setEnabled(false);
        })
        turn.setEnabled(true);
    }

    @Override
    void onTurnBasedMatchRemoved(String s) {
        Log.e("onTurnBasedMatchRemoved", "onTurnBasedMatchRemoved")
    }

    @Override
    void onBackPressed() {
        if( match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN){
            Games.TurnBasedMultiplayer.leaveMatchDuringTurn(client, match.getMatchId(), nextPlayerId(match))
        }else{
            Games.TurnBasedMultiplayer.leaveMatch(client,match.getMatchId())
        }
        super.onBackPressed()
    }

}
