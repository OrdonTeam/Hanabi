package com.ordonteam.hanabi.gms

import android.content.Intent
import android.os.Bundle
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig.Builder
import groovy.transform.CompileStatic

@CompileStatic
class GameConfig {
    static TurnBasedMatchConfig configFromIntent(Intent intent) {
        Builder builder = TurnBasedMatchConfig.builder()
        ArrayList<String> players = intent.getStringArrayListExtra(Games.EXTRA_PLAYER_IDS)
        if (players)
            builder.addInvitedPlayers(players)

        int min = intent.getIntExtra(
                Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 0);
        int max = intent.getIntExtra(
                Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 0);
        Bundle criteria = RoomConfig.createAutoMatchCriteria(min, max, 0);
        if (min >= 0 && max > 0)
            builder.setAutoMatchCriteria(criteria)


        return builder.build()
    }
}
