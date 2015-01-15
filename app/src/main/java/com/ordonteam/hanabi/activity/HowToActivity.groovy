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
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.how_to)
class HowToActivity extends InjectActivity{

    @InjectView(R.id.how_to_row)
    LinearLayout row

    @InjectView(R.id.how_to_game)
    GameInfoView gameInfoView

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle)
        CardView card0 = row.getChildAt(0) as CardView
        card0.setUserCard(new HanabiCard(CardColor.RED,CardValue.FIVE))
        CardView card1 = row.getChildAt(1) as CardView
        card1.setUserCard(new HanabiCard(CardColor.RED,CardValue.FIVE,true,false))
        CardView card2 = row.getChildAt(2) as CardView
        card2.setUserCard(new HanabiCard(CardColor.RED,CardValue.FIVE,false,true))
        CardView card3 = row.getChildAt(3) as CardView
        card3.setPlayerCard(new HanabiCard(CardColor.RED,CardValue.FIVE))
        CardView card4 = row.getChildAt(4) as CardView
        card4.setPlayerCard(new HanabiCard(CardColor.RED,CardValue.FIVE,true,false))
        CardView card5 = row.getChildAt(5) as CardView
        card5.setPlayerCard(new HanabiCard(CardColor.RED,CardValue.FIVE,false,true))

        gameInfoView.setRemainingCardsLeft(25);
        gameInfoView.setTopRejectedCard(new HanabiCard(CardColor.RED,CardValue.FIVE))
    }
}
