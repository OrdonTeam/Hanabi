package com.ordonteam.hanabi.activity

import android.content.DialogInterface
import android.graphics.Color
import android.util.Log
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.view.CardsRow
import com.ordonteam.hanabi.view.ColorNumberDialog
import com.ordonteam.hanabi.view.FullRow
import com.ordonteam.hanabi.view.GameInfoView
import com.ordonteam.hanabi.view.PlayRejectDialog
import com.ordonteam.hanabi.view.PlayerView
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AdditionalAbstractActivity implements CardsRow.OnCardClickListener {

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

    @Override
    byte[] newGameFor(int numberOfPlayers) {
        return new HanabiGame(numberOfPlayers).persist()
    }

    @Override
    void initGameField(int numberOfPlayers, int selfIndex) {
        otherPlayers().take(numberOfPlayers - 1).each { FullRow it ->
            it.setVisibility(LinearLayout.VISIBLE)
        }

        List<CardsRow> rows = otherPlayers()*.cardsRow
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setOnCardClickListener(this, (i + selfIndex + 1) % numberOfPlayers)
        }
        playerRow.cardsRow.setOnCardClickListener(this.&myCardRowClickPerform)
    }

    @Override
    void onMatchMyNextTurn(byte[] matchData) {
        updateGameView(matchData)
        otherPlayers().each {
            it.setBackgroundColor(Color.TRANSPARENT)
            it.playerView.setLetterColor(Color.LTGRAY)
        }
        dismissSpinner()
    }

    @Override
    void onMatchOtherNextTurn(byte[] matchData) {
        updateGameView(matchData)
        otherPlayers().each {
            it.setBackgroundColor(Color.TRANSPARENT)
            it.playerView.setLetterColor(Color.LTGRAY)
        }
        int index = (getPlayersNumber() + currentIndexOnGmsList() - myIndexOnGmsList() - 1) % getPlayersNumber()
        otherPlayers()[index].setBackgroundColor(Color.rgb(255, 150, 50))
        otherPlayers()[index].playerView.setLetterColor(Color.BLACK)
        showSpinner()
    }

    @Override
    void onMatchStatusComplete(byte[] matchData) {
        HanabiGame hanabi = HanabiGame.unpersist(matchData)
        dismissSpinner()
        int score = hanabi.score()

        unlock(R.string.achievement_first_match);
        increaseScore(R.string.leaderboard_total_points, score)
        if (score == 25) {
            unlock(R.string.achievement_first_perfect_firework);
            increment(R.string.achievement_3_prefect_fireworks);
            increment(R.string.achievement_5_perfect_fireworks);
            increaseScore(R.string.leaderboard_perfect_fireworks, 1)
        }
        if (hanabi.thundersNumber == 0) {
            unlock(R.string.achievement_bad_move);
        }
        Toast.makeText(this, "Match is finished", Toast.LENGTH_LONG).show()
    }

    @Override
    void onMatchStatusCanceled(byte[] data) {
        dismissSpinner()
        super.onBackPressed()
    }

    private void updateGameView(byte[] matchData) {
        List<PlayerView> rows = otherPlayers()*.playerView.take(getPlayersNumber() - 1)
        playerRow.playerView.setPlayerInfo(match.participants[myIndexOnGmsList()])
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setPlayerInfo(match.participants[(i + myIndexOnGmsList() + 1) % getPlayersNumber()])
        }
        HanabiGame hanabi = HanabiGame.unpersist(matchData)
        hanabi.updateCards(allCardsRows(), myIndexOnGmsList())
        hanabi.updatePlayedCards(playedCardsView)
        hanabi.updateGameInfo(gameInfoView)
        hanabi.updateLogs(logs, match.participants, myIndexOnGmsList())
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
            finishMatch(hanabi.persist())
        } else {
            takeTurn(hanabi.persist())
            Log.e('taking a turn','taking a turn')
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
