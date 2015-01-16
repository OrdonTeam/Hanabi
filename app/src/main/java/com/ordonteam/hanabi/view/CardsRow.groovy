package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.ordonteam.hanabi.activity.GameActivity
import com.ordonteam.hanabi.game.HanabiGame

class CardsRow extends LinearLayout {
    ArrayList<CardView> cardViewList
    OnCardClickListener onCardClickListener
    int row

    CardsRow(Context context, AttributeSet attrs) {
        super(context, attrs)
        setOrientation(this.HORIZONTAL)
        cardViewList = new ArrayList<>()
        LayoutParams layoutParams = new LayoutParams(context, attrs)
        layoutParams.weight = 1

               
        for (int i = 0; i < 5; i++) {
            CardView tmp = new CardView(context, attrs)
            tmp.setLayoutParams(layoutParams)
            cardViewList.add(tmp)
            tmp.setVisibility(GONE)
            addView(tmp)
        }
    }
    void setOnCardClickListener(OnCardClickListener listener) {
        setOnCardClickListener(listener,-1)
    }

    void setOnCardClickListener(OnCardClickListener listener, int row) {
        this.onCardClickListener = listener
        this.row = row
        cardViewList.eachWithIndex { CardView view, int i ->
            view.setOnClickListener({
                onCardClickListener?.onCardClicked(row, i)
            })
        }
    }

    void removeOnCardClickListener() {
        onCardClickListener = null;
    }

    interface OnCardClickListener {
        void onCardClicked(int row, int index)
    }
}
