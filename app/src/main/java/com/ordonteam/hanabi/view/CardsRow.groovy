package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams

class CardsRow extends LinearLayout{
    private ArrayList<CardView> cardViewList;

    CardsRow(Context context, AttributeSet attrs) {
        super(context, attrs)
        setOrientation(this.HORIZONTAL)
        cardViewList = new ArrayList<>()
        LayoutParams layoutParams = new LayoutParams(context,attrs)
        layoutParams.weight = 1
        for(int i=0; i<6; i++){
            CardView tmp = new CardView(context, attrs)
            tmp.setLayoutParams(layoutParams)
            cardViewList.add(tmp)
        }
        cardViewList.each {addView(it)}
    }
}
