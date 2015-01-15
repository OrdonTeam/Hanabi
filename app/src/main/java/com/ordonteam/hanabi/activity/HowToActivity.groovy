package com.ordonteam.hanabi.activity

import android.os.Bundle
import android.widget.LinearLayout
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.CardColor
import com.ordonteam.hanabi.game.CardValue
import com.ordonteam.hanabi.game.HanabiCard
import com.ordonteam.hanabi.view.CardView
import com.ordonteam.hanabi.view.GameInfoView
import com.ordonteam.inject.InjectActivity
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.how_to)
class HowToActivity extends InjectActivity{

    @InjectView(R.id.how_to_row)
    LinearLayout row

    @InjectView(R.id.how_to_row_2)
    LinearLayout row2

    @InjectView(R.id.how_to_game)
    GameInfoView gameInfoView

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle)
        initCards(row, 'setUserCard')
        initCards(row2, 'setPlayerCard')
        gameInfoView.setRemainingCardsLeft(25);
        gameInfoView.setTopRejectedCard(new HanabiCard(CardColor.RED,CardValue.FIVE))
    }

    @CompileDynamic
    private static void initCards(LinearLayout row, String method) {
        CardView card0 = row.getChildAt(0) as CardView
        card0."${method}"(new HanabiCard(CardColor.RED, CardValue.FIVE))
        CardView card1 = row.getChildAt(1) as CardView
        card1."${method}"(new HanabiCard(CardColor.RED, CardValue.FIVE, true, false))
        CardView card2 = row.getChildAt(2) as CardView
        card2."${method}"(new HanabiCard(CardColor.RED, CardValue.FIVE, false, true))
        CardView card3 = row.getChildAt(3) as CardView
        card3."${method}"(new HanabiCard(CardColor.RED, CardValue.FIVE, true, true))
    }
}
