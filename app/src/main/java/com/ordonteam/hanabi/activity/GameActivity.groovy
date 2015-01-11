package com.ordonteam.hanabi.activity

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.Player
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.ParticipantResult
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
import com.ordonteam.hanabi.view.FullRow
import com.ordonteam.hanabi.view.GameInfoView
import com.ordonteam.hanabi.view.PlayRejectDialog
import com.ordonteam.hanabi.view.PlayerView
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesMatchActivity implements CardsRow.OnCardClickListener {

    @InjectView(R.id.playerRow1)
    FullRow playerRow1
    @InjectView(R.id.playerRow2)
    FullRow playerRow2
    @InjectView(R.id.playerRow3)
    FullRow playerRow3
    @InjectView(R.id.playerRow4)
    FullRow playerRow4

    @InjectView(R.id.playedCardsView)
    CardsRow playedCardsView

    @InjectView(R.id.gameInfo)
    GameInfoView gameInfoView

    @InjectView(R.id.logs)
    TextView logs

    @InjectView(R.id.playerRow)
    FullRow playerRow

    @InjectView(R.id.spinner)
    RelativeLayout spinner

    private TurnBasedMatchConfig config
    private String invId
    private String matchId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        matchId = intent.getStringExtra(Multiplayer.EXTRA_TURN_BASED_MATCH)
        if (matchId)
            return
        invId = intent.getStringExtra(Multiplayer.EXTRA_INVITATION)
        if (invId)
            return
        config = GameConfig.configFromIntent(intent)
    }

    @Override
    void onConnected(Bundle connectionHint) {
        Games.TurnBasedMultiplayer.registerMatchUpdateListener(client, this)
        TurnBasedMatch match = connectionHint?.getParcelable(Multiplayer.EXTRA_TURN_BASED_MATCH) as TurnBasedMatch;
        if (match) {
            onTurnBasedMatchReceived(match);
        } else if (matchId) {
            loadMatch(matchId)
        } else if (invId) {
            acceptInvitation(invId)
        } else if (config) {
            createMatch(config)
        }
    }

    @Override
    void initMatch(TurnBasedMatch match) {
        this.match = match
        initGameField()
        HanabiGame hanabi = match?.data ? HanabiGame.unpersist(match.data) : new HanabiGame(getPlayersNumber())
        submitTurnToGoogleApi(hanabi)
    }

    private void initGameField() {
        otherPlayers().take(getPlayersNumber() - 1)
                .each { it.setVisibility(LinearLayout.VISIBLE) }

        List<CardsRow> rows = otherPlayers()*.cardsRow
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setOnCardClickListener(this, (i + myIndexOnGmsList() + 1) % getPlayersNumber())
        }
        playerRow.cardsRow.setOnCardClickListener(this.&myCardRowClickPerform)
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
        }else if(match?.status == TurnBasedMatch.MATCH_STATUS_COMPLETE) {
            dismissSpinner()
            Toast.makeText(this,"Match is finished",Toast.LENGTH_LONG).show()
        }
        this.match = match
        otherPlayers().each {
            it.setBackgroundColor(Color.TRANSPARENT)
        }
        if (isMyTurn()) {
            dismissSpinner()
        } else {
            if(match.getStatus() != TurnBasedMatch.MATCH_STATUS_AUTO_MATCHING){
                int index = (getPlayersNumber() + currentIndexOnGmsList() - myIndexOnGmsList() - 1)%getPlayersNumber()
                otherPlayers()[index].setBackgroundColor(Color.rgb(255,100,0))//TODO change to nice color
            }
            showSpinner()
        }
        updatePlayersInfo()
        HanabiGame hanabi = HanabiGame.unpersist(match.getData())
        hanabi.updateCards(allCardsRows(), myIndexOnGmsList())
        hanabi.updatePlayedCards(playedCardsView)
        hanabi.updateGameInfo(gameInfoView)
        hanabi.updateLogs(logs, match.participants, myIndexOnGmsList())
    }

    private void updatePlayersInfo() {
        List<PlayerView> rows = otherPlayers()*.playerView.take(getPlayersNumber() - 1)
        playerRow.playerView.setPlayerInfo(match.participants[myIndexOnGmsList()])
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setPlayerInfo(match.participants[(i + myIndexOnGmsList() + 1) % getPlayersNumber()])
        }
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
                if (hanabi.hintPlayerColor(chosenPlayer, cardIndex, myIndexOnGmsList()))
                    submitTurnToGoogleApi(hanabi)
                else
                    Toast.makeText(this, 'No tips left to make move', Toast.LENGTH_LONG).show()
            }, { DialogInterface dialog, int whichButton ->
                if (hanabi.hintPlayerNumber(chosenPlayer, cardIndex, myIndexOnGmsList()))
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
        if (hanabi.isGameFinished()) {
            List<ParticipantResult> participantResults = match.getParticipantIds().collect {
                new ParticipantResult(it, hanabi.score(), 1);
            }
            Games.TurnBasedMultiplayer.finishMatch(client, match.matchId, hanabi.persist(), participantResults).setResultCallback(this.&updateMatchResult)
        } else {
            Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, hanabi.persist(), nextPlayerId()).setResultCallback(this.&updateMatchResult)
            showSpinner()
        }
    }

    public void showSpinner() {
        spinner.setVisibility(RelativeLayout.VISIBLE)
    }

    public void dismissSpinner() {
        spinner.setVisibility(RelativeLayout.GONE)
    }

    private List<FullRow> otherPlayers() {
        return [playerRow1, playerRow2, playerRow3, playerRow4]
    }

    private List<CardsRow> allCardsRows() {
        return [playerRow.cardsRow] + otherPlayers()*.cardsRow
    }

}
