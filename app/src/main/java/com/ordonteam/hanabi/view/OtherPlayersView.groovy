package com.ordonteam.hanabi.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.android.gms.games.multiplayer.Participant
import com.ordonteam.hanabi.R
import com.ordonteam.inject.InjectActivity
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
class OtherPlayersView extends LinearLayout {
    @InjectView(R.id.playerRow1)
    FullRow playerRow1
    @InjectView(R.id.playerRow2)
    FullRow playerRow2
    @InjectView(R.id.playerRow3)
    FullRow playerRow3
    @InjectView(R.id.playerRow4)
    FullRow playerRow4

    OtherPlayersView(Context context) {
        super(context)
    }

    OtherPlayersView(Context context, AttributeSet attrs) {
        super(context, attrs)
    }

    OtherPlayersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle)
    }

    private List<FullRow> otherPlayers() {
        if (!playerRow1)
            InjectActivity.injectFieldsIn(this)
        return [playerRow1, playerRow2, playerRow3, playerRow4]
    }

    void show(int count) {
        otherPlayers().take(count).each { FullRow it ->
            it.setVisibility(VISIBLE)
        }
    }

    void setOnCardClickListener(CardsRowListener listener, int selfIndex, int numberOfPlayers) {
        List<CardsRow> rows = otherPlayers()*.cardsRow
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setOnCardClickListener(listener, (i + selfIndex + 1) % numberOfPlayers)
        }
    }

    void removeActive() {
        otherPlayers().each {
            it.setBackgroundColor(Color.TRANSPARENT)
            it.playerView.setLetterColor(Color.LTGRAY)
        }
    }

    void setActive(int index) {
        otherPlayers()[index].setBackgroundColor(Color.rgb(255, 150, 50))
        otherPlayers()[index].playerView.setLetterColor(Color.BLACK)
    }

    void removeOnCardClickListeners() {
        otherPlayers().each {
            it.cardsRow.removeOnCardClickListener()
        }
    }

    void setPlayersInfo(int numberOfPlayers, int selfIndex, ArrayList<Participant> participants) {
        List<PlayerView> rows = otherPlayers()*.playerView.take(numberOfPlayers - 1)
        for (int i = 0; i < rows.size(); i++) {
            rows[i].setPlayerInfo(participants[(i + selfIndex + 1) % numberOfPlayers])
        }
    }
}
