package com.ordonteam.hanabi.view

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import com.ordonteam.hanabi.activity.GameActivity
import com.ordonteam.hanabi.game.HanabiGame
import groovy.transform.CompileStatic

@CompileStatic
class CardsRowListener implements CardsRow.OnCardClickListener{
    private HanabiGame hanabiGame
    private GameActivity gameActivity
    private int selfIndex

    CardsRowListener(HanabiGame hanabiGame, GameActivity gameActivity, int selfIndex) {
        this.hanabiGame = hanabiGame
        this.gameActivity = gameActivity
        this.selfIndex = selfIndex
    }

    @Override
    void onCardClicked(int row, int index) {
        Log.i("tag", "chosenPlayer ${row} index ${index} ")
        new ColorNumberDialog(gameActivity).setButtonsAction({ DialogInterface dialog, int whichButton ->
            if (hanabiGame.hintPlayerColor(row, index, selfIndex))
                gameActivity.submitTurn(hanabiGame)
            else
                Toast.makeText(gameActivity, 'No tips left to make move', Toast.LENGTH_LONG).show()
        }, { DialogInterface dialog, int whichButton ->
            if (hanabiGame.hintPlayerNumber(row, index, selfIndex))
                gameActivity.submitTurn(hanabiGame)
            else
                Toast.makeText(gameActivity, 'No tips left to make move', Toast.LENGTH_LONG).show()
        }).show()
    }
}
