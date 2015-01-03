package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import com.ordonteam.hanabi.activity.GameActivity

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
        }
        cardViewList.each {
            addView(it)
        }
    }

    void setOnCardClickListener(OnCardClickListener gameActivity, int row) {
        this.onCardClickListener = gameActivity
        this.row = row
        cardViewList.eachWithIndex { CardView view, int i ->
            view.setOnClickListener({
                onCardClickListener?.onCardClicked(row, i+1)
            })
        }
    }

    interface OnCardClickListener {
        void onCardClicked(int row, int index)
    }


}
