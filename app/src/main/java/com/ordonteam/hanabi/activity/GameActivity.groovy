package com.ordonteam.hanabi.activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.actions.HintPlayerAction
import com.ordonteam.hanabi.gms.AbstractGamesActivity
import com.ordonteam.hanabi.gms.GameConfig
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
        String next = nextPlayerId(result.match)
        if (result.match?.data) {
            HanabiGame hanabi = HanabiGame.unpersist(result.match.data)
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, hanabi.persist(), next)
                    .setResultCallback(this.&updateMatchResult)
        } else {
            HanabiGame hanabi = new HanabiGame()
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, hanabi.persist(), next)
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
            return participantIds.get(desiredIndex);
        }

        if (match.getAvailableAutoMatchSlots() <= 0) {
            return participantIds.get(0);
        } else {
            return null;
        }
    }

    void updateMatchResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        if(result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_OK){
            this.match = result.match
        }else{
            Log.w("updateMatchResult",'status code is not ok')
        }
    }

    @Override
    void onTurnBasedMatchReceived(TurnBasedMatch match) {
        if( match?.status == TurnBasedMatch.MATCH_STATUS_CANCELED ){
            super.onBackPressed()
        }
        this.match = match
        HanabiGame hanabi = HanabiGame.unpersist(match.getData())

        if( match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN) {
            String next = nextPlayerId(match)
            turn.setOnClickListener({
                Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, hanabi.persist(), next).setResultCallback(this.&updateMatchResult)
                turn.setEnabled(false);
            })
            turn.setEnabled(true);
        }
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

    void makeSomeAction(){
        HanabiGame hanabi = HanabiGame.unpersist(match.getData())
//        hanabi.makeAction(new HintPlayerAction.aHintPlayerAction().withSourcePlayer(1))
    }

}
