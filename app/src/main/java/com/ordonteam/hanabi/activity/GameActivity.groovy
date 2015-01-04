package com.ordonteam.hanabi.activity

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.turnbased.OnTurnBasedMatchUpdateReceivedListener
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer.InitiateMatchResult
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.HanabiGame
import com.ordonteam.hanabi.game.actions.HintPlayerColor
import com.ordonteam.hanabi.game.actions.HintPlayerNumber
import com.ordonteam.hanabi.game.actions.PutCardPlayerAction
import com.ordonteam.hanabi.game.actions.RejectPlayerAction
import com.ordonteam.hanabi.gms.AbstractGamesActivity
import com.ordonteam.hanabi.gms.GameConfig
import com.ordonteam.hanabi.view.CardsRow
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

//TODO: when loading dialog visible after returning from home button
//java.lang.NullPointerException: Attempt to invoke interface method 'java.util.ArrayList com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch.getParticipantIds()' on a null object reference
//at com.ordonteam.hanabi.activity.GameActivity.getPlayersNumber(GameActivity.groovy:105)
//at com.ordonteam.hanabi.activity.GameActivity.initiateMatchResult(GameActivity.groovy:96)
//at java.lang.reflect.Method.invoke(Native Method)
//at java.lang.reflect.Method.invoke(Method.java:372)
//at org.codehaus.groovy.reflection.CachedMethod.invoke(CachedMethod.java:90)
//at groovy.lang.MetaMethod.doMethodInvoke(MetaMethod.java:324)
//at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1208)
//at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1075)
//at groovy.lang.MetaClassImpl.invokeMethod(MetaClassImpl.java:1017)
//at groovy.lang.Closure.call(Closure.java:423)
//at org.codehaus.groovy.runtime.ConvertedClosure.invokeCustom(ConvertedClosure.java:51)
//at org.codehaus.groovy.runtime.ConversionHandler.invoke(ConversionHandler.java:103)

@CompileStatic
@InjectContentView(R.layout.game_layout)
class GameActivity extends AbstractGamesActivity implements OnTurnBasedMatchUpdateReceivedListener, CardsRow.OnCardClickListener {

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

    private TurnBasedMatchConfig config
    private String invId
    private TurnBasedMatch match
    private ProgressDialog progressDialog

    @Override
    @CompileDynamic
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        invId = intent.getStringExtra(Multiplayer.EXTRA_INVITATION)
        if (!invId) {
            config = GameConfig.configFromIntent(intent)
        }
        getCooperatorCardRows().each {
            it.setOnCardClickListener(this, it.row)
        }
        getMyCardRow().setOnCardClickListener(this.&myCardRowClickPerform,5)

        progressDialog = new ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Patience is a virtue...");
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
        (getPlayersNumber(match)-1).times{
            getLinears().get(it).setVisibility(LinearLayout.VISIBLE)
        }

        HanabiGame hanabi = result.match?.data ? HanabiGame.unpersist(result.match.data) : new HanabiGame(getPlayersNumber(match))
        submitTurnToGoogleApi(hanabi)
    }

    private int getPlayersNumber(TurnBasedMatch match) {
        match.getParticipantIds().size() + match.availableAutoMatchSlots
    }

    private int myIndexOnGmsList() {
        String playerId = Games.Players.getCurrentPlayerId(client);
        String myParticipantId = match.getParticipantId(playerId);
        List<String> participantIds = match.getParticipantIds();

        return participantIds.indexOf(participantIds.find{
            it == myParticipantId
        })
    }

    private String nextPlayerId(TurnBasedMatch match) {
        String playerId = Games.Players.getCurrentPlayerId(client);
        String myParticipantId = match.getParticipantId(playerId);

        return nextPlayerId(match, myParticipantId)
    }

    private String nextPlayerId(TurnBasedMatch match, String myParticipantId) {
        int desiredIndex = -1;

        List<String> participantIds = match.getParticipantIds();

        participantIds.size().times {
            if (participantIds.get(it) == myParticipantId) {
                desiredIndex = it + 1;
            }
        }

        if (desiredIndex < participantIds.size()) {
            return participantIds.get(desiredIndex);
        }

        if (match.getAvailableAutoMatchSlots() <= 0) {
            return participantIds.get(0);
        } else {
            return null;
        }
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
        if(isMyTurn() ){
            dismissSpinner()
        }else{
            showSpinner()
        }
        HanabiGame hanabi = HanabiGame.unpersist(match.getData())
        hanabi.updateCards(getAllRows(), myIndexOnGmsList())
        hanabi.updatePlayedCards(playedCardsView)
    }

    @Override
    void onTurnBasedMatchRemoved(String s) {
        Log.e("onTurnBasedMatchRemoved", "onTurnBasedMatchRemoved")
    }

    @Override
    void onBackPressed() {
        if (match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN) {
            Games.TurnBasedMultiplayer.leaveMatchDuringTurn(client, match.getMatchId(), nextPlayerId(match))
        } else {
            Games.TurnBasedMultiplayer.leaveMatch(client, match.getMatchId())
            dismissSpinner()
        }
        super.onBackPressed()
    }

    private submitTurnToGoogleApi(HanabiGame hanabi) {
        Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, hanabi.persist(), nextPlayerId(match)).setResultCallback(this.&updateMatchResult)
        showSpinner()
    }

    void onCardClicked(int row, int index) {
        Log.i("tag", "row $row index $index ")

        if( isMyTurn() ) {
            HanabiGame hanabi = HanabiGame.unpersist(match.getData())
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Hint");
            alert.setMessage("You want to give a hint about:");

            alert.setPositiveButton("color", { DialogInterface dialog, int whichButton ->
                int chosenPlayer = convertRowToHanabiIndex(row)
                new HintPlayerColor(chosenPlayer, index, myIndexOnGmsList()).doAction(hanabi)
                submitTurnToGoogleApi(hanabi)

            });

            alert.setNegativeButton("number", { DialogInterface dialog, int whichButton ->
                int chosenPlayer = convertRowToHanabiIndex(row)
                new HintPlayerNumber(chosenPlayer, index, myIndexOnGmsList()).doAction(hanabi)
                submitTurnToGoogleApi(hanabi)
            });

            alert.show();
        }else{
            Log.i("tag", "no my turn ${match.getTurnStatus()}")
        }
    }

    private int convertRowToHanabiIndex(int row) {
        return (row + myIndexOnGmsList() + getPlayersNumber(match) - 1) % getPlayersNumber(match)
    }

    void myCardRowClickPerform(int row, int index) {//TODO: android.view.WindowLeaked: Activity com.ordonteam.hanabi.activity.GameActivity has leaked window when gameCanceled
        Log.i("tag", "row $row index $index ")
        if(isMyTurn()){
            dismissSpinner()
            HanabiGame hanabi = HanabiGame.unpersist(match.getData())
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Action");
            alert.setMessage("What do you want to do?");

            alert.setPositiveButton("Play the card", { DialogInterface dialog, int whichButton ->
                new PutCardPlayerAction(index, myIndexOnGmsList()).doAction(hanabi)
                submitTurnToGoogleApi(hanabi)
            });

            alert.setNegativeButton("Reject the card", { DialogInterface dialog, int whichButton ->
                new RejectPlayerAction(index, myIndexOnGmsList()).doAction(hanabi)
                submitTurnToGoogleApi(hanabi)
            });

            alert.show();
            Log.i("tag", "my turn")
        }else{
            Log.i("tag", "no my turn ${match.getTurnStatus()}")
        }

    }

    @CompileDynamic
    private CardsRow getCardRowByIndex(int index) {
        return this."row${index}"
    }

    private List<CardsRow> getCooperatorCardRows() {
        return (1..4).collect {
            getCardRowByIndex(it)
        }
    }

    private List<LinearLayout> getLinears() {
        return [playerRow1,playerRow2,playerRow3,playerRow4]
    }

    private CardsRow getMyCardRow() {
        return row5
    }

    private boolean isMyTurn() {
        return match.getTurnStatus() == TurnBasedMatch.MATCH_TURN_STATUS_MY_TURN
    }

    private List<CardsRow> getAllRows(){
        return getCooperatorCardRows() + getMyCardRow()
    }

    public void showSpinner() {
        progressDialog.show()
    }

    public void dismissSpinner() {
        progressDialog.dismiss()
    }

}
