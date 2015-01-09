package com.ordonteam.hanabi.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Invitation
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener
import com.ordonteam.hanabi.gms.AbstractGamesActivity
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
class MainActivity extends AbstractGamesActivity implements OnInvitationReceivedListener {

    @InjectView(R.id.modeChooser)
    LinearLayout modeChooser

    @Override
    void onConnected(Bundle bundle) {
        Log.e('MainActivity', 'onConnected')
        Button buttonPlay = (Button)findViewById(R.id.play)
        Button buttonInvite = (Button)findViewById(R.id.invite)
        buttonPlay.setEnabled(true)
        buttonInvite.setEnabled(true)
        Games.Invitations.registerInvitationListener(client, this);
        modeChooser.setVisibility(View.VISIBLE)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @InjectClickListener(R.id.play)
    void play(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Random match");
        alert.setMessage("How many people do you want to play with?");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Integer value = Integer.parseInt(input.getText().toString());
                playMatchedRandomly(value);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    @Override
    void onInvitationReceived(Invitation invitation) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Invitation");
        alert.setMessage("You've been invited by ${invitation.getInviter().displayName} " +
                "to join the game. Do you accept the invitation?");

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startGameForInvitation(invitation.invitationId)
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {}
        });

        alert.show();
    }

    private void startGameForInvitation(String invitationId) {
        Intent gameActivity = new Intent(this, GameActivity)
        gameActivity.putExtra(Multiplayer.EXTRA_INVITATION, invitationId);
        startActivity(gameActivity);
    }

    void playMatchedRandomly(int i) {
        Intent gameActivity = new Intent(this, GameActivity)
        gameActivity.putExtra(Multiplayer.EXTRA_MIN_AUTOMATCH_PLAYERS, i);
        gameActivity.putExtra(Multiplayer.EXTRA_MAX_AUTOMATCH_PLAYERS, i);
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

    @Override
    void onInvitationRemoved(String s) {
        Log.e("onInvitationRemoved", "onInvitationRemoved")
    }
}
