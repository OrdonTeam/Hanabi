package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout;

class FullRow extends LinearLayout {
    FullRow(Context context) {
        super(context)
    }

    FullRow(Context context, AttributeSet attrs) {
        super(context, attrs)
    }

    FullRow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle)
    }

    PlayerView getPlayerView(){
        return findViewByClass(PlayerView)
    }

    CardsRow getCardsRow(){
        return findViewByClass(CardsRow)
    }

    private <T> T findViewByClass(Class<T> aClass ) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (aClass.isInstance(view))
                return view as T
        }
        return null
    }
}
