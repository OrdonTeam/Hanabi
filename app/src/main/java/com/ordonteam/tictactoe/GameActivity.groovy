package com.ordonteam.tictactoe

import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Status
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.gms.AbstractGamesActivity
import com.ordonteam.inject.InjectContentView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesActivity{

    private TurnBasedMatchConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        ArrayList<String> players = intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS)?:
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

    void initiateMatchResult(InitiateMatchResult initiateMatchResult){
        Status status = initiateMatchResult.getStatus()
    }
}
