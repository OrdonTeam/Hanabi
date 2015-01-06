package com.ordonteam.hanabi.activity

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.gms.AbstractGamesMatchActivity
import com.ordonteam.hanabi.gms.GameConfig
import com.ordonteam.hanabi.view.CardsRow
import com.ordonteam.hanabi.view.ColorNumberDialog
import com.ordonteam.hanabi.view.GameInfoView
import com.ordonteam.hanabi.view.PlayRejectDialog
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesMatchActivity implements CardsRow.OnCardClickListener {

    @InjectView(R.id.playerCardRow1)
    CardsRow row1
    @InjectView(R.id.playerCardRow2)
    CardsRow row2
    @InjectView(R.id.playerCardRow3)
    CardsRow row3
    @InjectView(R.id.playerCardRow4)
    CardsRow row4

    @InjectView(R.id.playerCardRow5)
    CardsRow row5

    @InjectView(R.id.playedCardsView)
    CardsRow playedCardsView

    @InjectView(R.id.playerRow1)
    LinearLayout playerRow1
    @InjectView(R.id.playerRow2)
    LinearLayout playerRow2
    @InjectView(R.id.playerRow3)
    LinearLayout playerRow3
    @InjectView(R.id.playerRow4)
    LinearLayout playerRow4

    @InjectView(R.id.gameInfo)
    GameInfoView gameInfoView

    @InjectView(R.id.spinner)
    RelativeLayout spinner

    private TurnBasedMatchConfig config
    private String invId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        invId = intent.getStringExtra(Multiplayer.EXTRA_INVITATION)
        if (!invId) {
            config = GameConfig.configFromIntent(intent)
        }
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
        match = result.match
        initGameField()
        HanabiGame hanabi = match?.data ? HanabiGame.unpersist(match.data) : new HanabiGame(getPlayersNumber())
        submitTurnToGoogleApi(hanabi)
    }

    private void initGameField() {
        [playerRow1, playerRow2, playerRow3, playerRow4]
                .take(getPlayersNumber() - 1)
                .each { it.setVisibility(LinearLayout.VISIBLE) }

        List<CardsRow> rows = [row1, row2, row3, row4]
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setOnCardClickListener(this, (i + myIndexOnGmsList() + 1) % getPlayersNumber())
        }
        row5.setOnCardClickListener(this.&myCardRowClickPerform)
    }

    void updateMatchResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        if (result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_OK) {
            onTurnBasedMatchReceived(result.match)
        } else {
            Log.w("updateMatchResult", 'status code is not ok')
        }
    }

    @Override
    void onTurnBasedMatchReceived(TurnBasedMatch match) {
        if (match?.status == TurnBasedMatch.MATCH_STATUS_CANCELED) {
            dismissSpinner()
            super.onBackPressed()
        }
        this.match = match
        if (isMyTurn()) {
            dismissSpinner()
        } else {
            showSpinner()
        }
        HanabiGame hanabi = HanabiGame.unpersist(match.getData())
        hanabi.updateCards(([row5, row1, row2, row3, row4]), myIndexOnGmsList())
        hanabi.updatePlayedCards(playedCardsView)
        hanabi.updateGameInfo(gameInfoView)
    }

    @Override
    void onBackPressed() {
        leaveMatch()
        dismissSpinner()
        super.onBackPressed()
    }

    void onCardClicked(int chosenPlayer, int cardIndex) {
        Log.i("tag", "chosenPlayer $chosenPlayer index $cardIndex ")
        if (isMyTurn()) {
            HanabiGame hanabi = HanabiGame.unpersist(match.getData())
            new ColorNumberDialog(this).setButtonsAction({ DialogInterface dialog, int whichButton ->
                if (hanabi.hintPlayerColor(chosenPlayer, cardIndex))
                    submitTurnToGoogleApi(hanabi)
                else
                    Toast.makeText(this, 'No tips left to make move', Toast.LENGTH_LONG).show()
            }, { DialogInterface dialog, int whichButton ->
                if (hanabi.hintPlayerNumber(chosenPlayer, cardIndex))
                    submitTurnToGoogleApi(hanabi)
                else
                    Toast.makeText(this, 'No tips left to make move', Toast.LENGTH_LONG).show()
            }).show();
        } else {
            Log.i("tag", "no my turn ${match.getTurnStatus()}")
        }
    }

    void myCardRowClickPerform(int row, int index) {
        Log.i("tag", "row $row index $index ")
        if (isMyTurn()) {
            HanabiGame hanabi = HanabiGame.unpersist(match.getData())
            new PlayRejectDialog(this).setButtonsAction({ DialogInterface dialog, int whichButton ->
                hanabi.playPlayerCard(myIndexOnGmsList(), index)
                submitTurnToGoogleApi(hanabi)
            }, { DialogInterface dialog, int whichButton ->
                hanabi.rejectPlayerCard(myIndexOnGmsList(), index)
                submitTurnToGoogleApi(hanabi)
            }).show();
        } else {
            Log.i("tag", "no my turn ${match.getTurnStatus()}")
        }
    }

    private submitTurnToGoogleApi(HanabiGame hanabi) {
        Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, hanabi.persist(), nextPlayerId()).setResultCallback(this.&updateMatchResult)
        showSpinner()
    }

    public void showSpinner() {
        spinner.setVisibility(RelativeLayout.VISIBLE)
    }

    public void dismissSpinner() {
        spinner.setVisibility(RelativeLayout.GONE)
    }

}
