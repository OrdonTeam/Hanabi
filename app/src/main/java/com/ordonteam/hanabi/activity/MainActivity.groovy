package com.ordonteam.hanabi.activity

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import com.google.android.gms.games.Games
import com.google.android.gms.games.multiplayer.Invitation
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.ordonteam.hanabi.dialog.HowManyDialog
import com.ordonteam.hanabi.dialog.LeaderboardsDialog
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
    @InjectView(R.id.play)
    Button buttonPlay
    @InjectView(R.id.invite)
    Button buttonInvite
    @InjectView(R.id.achievements)
    Button buttonAchievements
    @InjectView(R.id.leaderboard)
    Button buttonLeaderboard
    @InjectView(R.id.moreView)
    LinearLayout moreView

    @Override
    void onConnected(Bundle connectionHint) {
        TurnBasedMatch match = connectionHint?.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH) as TurnBasedMatch;
        if (match) {
            Intent gameActivity = new Intent(this, GameActivity)
            gameActivity.putExtra(Multiplayer.EXTRA_TURN_BASED_MATCH, match.matchId);
            startActivity(gameActivity)
            Log.e('MainActivity', "onConnected ${match?.participants?.get(0)?.displayName}")
        } else {
            buttonPlay.setEnabled(true)
            buttonInvite.setEnabled(true)
            buttonAchievements.setEnabled(true)
            buttonLeaderboard.setEnabled(true)
            Games.Invitations.registerInvitationListener(client, this);
            modeChooser.setVisibility(View.VISIBLE)
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @InjectClickListener(R.id.play)
    void play(View view) {
        new HowManyDialog(this).setOnHowManyChooseListener({
            playMatchedRandomly(it);
        }).show();
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

    @InjectClickListener(R.id.moreButton)
    void more(View view){
        view.setVisibility(LinearLayout.GONE)
        moreView.setVisibility(LinearLayout.VISIBLE)
    }

    @InjectClickListener(R.id.achievements)
    void achievements(View view){
        startActivityForResult(Games.Achievements.getAchievementsIntent(client),
                InjectConstants.REQUEST_ACHIEVEMENTS);
    }

    @InjectClickListener(R.id.leaderboard)
    void leaderboard(View view){
        AlertDialog.Builder alert = new LeaderboardsDialog(this);
        alert.show();
    }

    @InjectClickListener(R.id.howTo)
    void howToPlay(View view){
        Intent intent = new Intent(this, HowToActivity)
        startActivity(intent)
    }

    @InjectClickListener(R.id.about)
    void about(View view){
        Intent intent = new Intent(this, AboutActivity)
        startActivity(intent)
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
