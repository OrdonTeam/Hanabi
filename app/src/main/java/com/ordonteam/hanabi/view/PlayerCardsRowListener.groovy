package com.ordonteam.hanabi.view

import android.content.DialogInterface
import android.util.Log
import com.ordonteam.hanabi.activity.GameActivity
import com.ordonteam.hanabi.game.HanabiGame
import groovy.transform.CompileStatic

@CompileStatic
class PlayerCardsRowListener implements CardsRow.OnCardClickListener{
    private HanabiGame hanabiGame
    private GameActivity gameActivity

    PlayerCardsRowListener(HanabiGame hanabiGame, GameActivity gameActivity) {
        this.hanabiGame = hanabiGame
        this.gameActivity = gameActivity
    }

    @Override
    void onCardClicked(int row, int index) {
        Log.i("tag", "row ${row} index ${index} ")
        new PlayRejectDialog(gameActivity).setButtonsAction({ DialogInterface dialog, int whichButton ->
            hanabiGame.playPlayerCard(gameActivity.myIndexOnGmsList(), index)
            gameActivity.submitTurn(hanabiGame)
        }, { DialogInterface dialog, int whichButton ->
            hanabiGame.rejectPlayerCard(gameActivity.myIndexOnGmsList(), index)
            gameActivity.submitTurn(hanabiGame)
        }).show()
    }
}
