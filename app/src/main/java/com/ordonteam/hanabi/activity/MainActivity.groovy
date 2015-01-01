package com.ordonteam.hanabi.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.google.android.gms.games.multiplayer.Multiplayer
import com.ordonteam.gms.AbstractGamesActivity
import com.ordonteam.hanabi.R
import com.ordonteam.inject.InjectActivityResult
import com.ordonteam.inject.InjectClickListener
import com.ordonteam.hanabi.inject.InjectConstants
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

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
        startActivityForResult(intent, InjectConstants.RC_SELECT_PLAYERS);
    }

    @InjectActivityResult(requestCode = InjectConstants.RC_SELECT_PLAYERS, responseCode = InjectConstants.RESULT_OK)
    void onPlayersSelected(int requestCode, int responseCode, Intent intent) {
        Intent gameActivity = new Intent(this, GameActivity)
        gameActivity.putExtras(intent)
        startActivity(gameActivity)
    }
}
