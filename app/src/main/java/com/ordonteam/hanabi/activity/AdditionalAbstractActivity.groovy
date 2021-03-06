package com.ordonteam.hanabi.activity

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.games.Games
import com.google.android.gms.games.GamesStatusCodes
import com.google.android.gms.games.leaderboard.LeaderboardScore
import com.google.android.gms.games.leaderboard.LeaderboardVariant
import com.google.android.gms.games.leaderboard.Leaderboards
import com.google.android.gms.games.multiplayer.Multiplayer
import com.google.android.gms.games.multiplayer.ParticipantResult
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatch
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMatchConfig
import com.google.android.gms.games.multiplayer.turnbased.TurnBasedMultiplayer
import com.ordonteam.hanabi.gms.AbstractGamesMatchActivity
import com.ordonteam.hanabi.gms.GameConfig
import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
abstract class AdditionalAbstractActivity extends AbstractGamesMatchActivity {
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
        initGameField(getPlayersNumber(), myIndexOnGmsList())
        byte[] hanabi = match?.data ? match.data : newGameFor(getPlayersNumber())
        takeTurn(hanabi)
    }

    abstract byte[] newGameFor(int numberOfPlayers);

    void updateMatchResult(TurnBasedMultiplayer.UpdateMatchResult result) {
        if (result.getStatus().getStatusCode() == GamesStatusCodes.STATUS_OK) {
            onTurnBasedMatchReceived(result.match)
        } else {
            Log.w("updateMatchResult", 'status code is not ok')
        }
    }

    abstract void initGameField(int numberOfPlayers, int selfIndex)

    @Override
    void onTurnBasedMatchReceived(TurnBasedMatch match) {
        this.match = match
        if (match?.status == TurnBasedMatch.MATCH_STATUS_CANCELED) {
            onMatchStatusCanceled(match.data)
        } else if (match?.status == TurnBasedMatch.MATCH_STATUS_COMPLETE) {
            onMatchNextTurn(match)
            onMatchStatusComplete(match.getData())
        } else {
            onMatchNextTurn(match)
        }
    }

    void onMatchNextTurn(TurnBasedMatch match) {
        if (isMyTurn()) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
            Notification notification = builder.build()
            notification.defaults |= Notification.DEFAULT_SOUND
            notification.defaults |= Notification.DEFAULT_VIBRATE
            NotificationManagerCompat notificationManager =   NotificationManagerCompat.from(this);
            notificationManager.notify(1, notification);
            onMatchMyNextTurn(match.getData(), getPlayersNumber(), myIndexOnGmsList())
        } else {
            onMatchOtherNextTurn(match.getData(), getPlayersNumber(), myIndexOnGmsList(), currentIndexOnGmsList())
        }
    }

    abstract void onMatchStatusCanceled(byte[] matchData)

    abstract void onMatchStatusComplete(byte[] matchData)

    abstract void onMatchMyNextTurn(byte[] matchData, int numberOfPlayers, int selfIndex)

    abstract void onMatchOtherNextTurn(byte[] matchData, int numberOfPlayers, int selfIndex, int current)

    void unlock(int id) {
        Games.Achievements.unlock(client, getString(id))
    }

    void increaseScore(int id, int score) {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(client, getString(id), LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC)
                .setResultCallback(new IncreaseScoreCallback(id,score))
    }

    void increment(int id) {
        Games.Achievements.increment(client, getString(id), 1)
    }

    void takeTurn(byte[] matchData) {
        Games.TurnBasedMultiplayer.takeTurn(client, match.matchId, matchData, nextPlayerId()).setResultCallback(this.&updateMatchResult)
    }

    void finishMatch(byte[] matchData) {
        List<ParticipantResult> participantResults = match.getParticipantIds().collect {
            new ParticipantResult(it, ParticipantResult.MATCH_RESULT_WIN, 1)
        }
        Games.TurnBasedMultiplayer.finishMatch(client, match.matchId, matchData, participantResults).setResultCallback(this.&updateMatchResult)
    }

    class IncreaseScoreCallback implements ResultCallback<Leaderboards.LoadPlayerScoreResult>{
        final int id
        final int score

        IncreaseScoreCallback(int id, int score) {
            this.id = id
            this.score = score
        }

        @Override
        void onResult(Leaderboards.LoadPlayerScoreResult result) {
            LeaderboardScore currentScore = result?.getScore()
            if (currentScore)
                Games.Leaderboards.submitScore(client, getString(id), currentScore.getRawScore() + score);
            else
                Games.Leaderboards.submitScore(client, getString(id), score);
        }
    }
}
