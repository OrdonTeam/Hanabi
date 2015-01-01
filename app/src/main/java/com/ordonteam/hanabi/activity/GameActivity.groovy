package com.ordonteam.hanabi.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.common.api.Status
import com.google.android.gms.games.Games
import com.google.android.gms.games.Player
import com.google.android.gms.games.Players
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.gms.AbstractGamesActivity
import com.ordonteam.hanabi.R
import com.ordonteam.inject.InjectClickListener
import com.ordonteam.inject.InjectContentView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesActivity {

    private TurnBasedMatchConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        ArrayList<String> players = intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS) ?:
                new ArrayList<String>()
        int min = intent.getIntExtra(
                Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int max = intent.getIntExtra(
                Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        Bundle criteria = RoomConfig.createAutoMatchCriteria(min, max, 0);
        config = TurnBasedMatchConfig.builder()
                .addInvitedPlayers(players)
                .setAutoMatchCriteria(criteria).build()
    }

    @Override
    void onConnected(Bundle bundle) {
        Games.TurnBasedMultiplayer.createMatch(client, config).setResultCallback(this.&initiateMatchResult)
    }

    void initiateMatchResult(InitiateMatchResult result) {
        Log.e("status", "result=${result.status}")
        Log.e("status", "match=${result.match?.status}")
        Log.e("status", "turn=${result.match?.turnStatus}")
        if (result.match?.data) {
            Log.e("status", "data=${new String(result.match.data)}")
            //This null is wrong but only here
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, 'second'.bytes, null).setResultCallback(this.&updateMatchResult)
        } else {
            Games.TurnBasedMultiplayer.takeTurn(client, result.match.matchId, 'first'.bytes, null).setResultCallback(this.&updateMatchResult)
        }
    }

    void updateMatchResult(TurnBasedMultiplayer.UpdateMatchResult result) {
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
}
