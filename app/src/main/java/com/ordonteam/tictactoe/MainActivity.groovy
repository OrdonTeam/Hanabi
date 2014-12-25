package com.ordonteam.tictactoe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.realtime.RoomConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.ordonteam.gms.AbstractGamesActivity
import com.ordonteam.inject.InjectClickListener
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

import static android.app.Activity.RESULT_OK
import static com.google.android.gms.games.Games.TurnBasedMultiplayer

@CompileStatic
@InjectContentView(R.layout.main_layout)
class MainActivity extends AbstractGamesActivity {

    @InjectView(R.id.modeChooser)
    LinearLayout modeChooser

    @Override
    void onConnected(Bundle bundle) {
        Log.e('MainActivity', 'onConnected')
        modeChooser.setVisibility(View.VISIBLE)
    }

    @InjectClickListener(R.id.play)
    void play(View view) {
        Intent gameActivity = new Intent(this, GameActivity)
        intent.putExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, 1);
        intent.putExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, 1);
        startActivity(gameActivity)
    }

    @InjectClickListener(R.id.invite)
    void invite(View view) {
        Intent intent = TurnBasedMultiplayer.getSelectOpponentsIntent(client, 1, 4, true);
        startActivityForResult(intent, RC_SELECT_PLAYERS);
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SELECT_PLAYERS) {
            if (responseCode == RESULT_OK) {
                Intent gameActivity = new Intent(this, GameActivity)
                gameActivity.putExtras(intent)
                startActivity(gameActivity)
            }
            return;
        }
        super.onActivityResult(requestCode, responseCode, intent)
    }
}
