package com.ordonteam.hanabi.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
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

import static com.google.android.gms.games.Games.Invitations
import static com.google.android.gms.games.Games.TurnBasedMultiplayer

@CompileStatic
@InjectContentView(R.layout.main_layout)
class MainActivity extends AbstractGamesActivity implements OnInvitationReceivedListener {

    @InjectView(R.id.modeChooser)
    LinearLayout modeChooser

    @Override
    void onConnected(Bundle bundle) {
        Log.e('MainActivity', 'onConnected')
        Games.Invitations.registerInvitationListener(client, this);
        modeChooser.setVisibility(View.VISIBLE)
    }

    @InjectClickListener(R.id.play)
    void play(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Random match");
        alert.setMessage("How many people do you want to play with?");
// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                Integer value = Integer.parseInt(input.getText().toString());
                PlayMatchedRandomly(value);
                // Do something with value!
            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    void PlayMatchedRandomly(int i) {
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
    void onInvitationReceived(Invitation invitation) {
        Log.e("onInvitationReceived", "onInvitationReceived")
        Intent gameActivity = new Intent(this, GameActivity)
        gameActivity.putExtra(Multiplayer.EXTRA_INVITATION, invitation.invitationId);
        startActivity(gameActivity)

//        Intent intent = Invitations.getInvitationInboxIntent(client)
//        startActivityForResult(intent,InjectConstants.RC_INVITATIONS)
    }

    @InjectActivityResult(requestCode = InjectConstants.RC_INVITATIONS, responseCode = InjectConstants.RESULT_OK)
    void onInvitationAccepted(int requestCode, int responseCode, Intent intent) {


    }

    @Override
    void onInvitationRemoved(String s) {
        Log.e("onInvitationRemoved", "onInvitationRemoved")
    }
}
