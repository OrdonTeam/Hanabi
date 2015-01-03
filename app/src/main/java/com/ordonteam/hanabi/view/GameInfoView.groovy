package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.ordonteam.hanabi.R
import android.widget.LinearLayout.LayoutParams


class GameInfoView extends LinearLayout {

    private ImageView hintsImg
    private ImageView thunderImg
    private CardView knownCards
    GameInfoView(Context context, AttributeSet attrs) {
        super(context, attrs)

        setOrientation(this.HORIZONTAL)

        hintsImg = new ImageView(context)
        thunderImg = new ImageView(context)
        knownCards = new CardView(context,attrs)

        LayoutParams layoutParams = new LayoutParams(context,attrs)
        layoutParams.weight = 1

        hintsImg.setImageResource(R.drawable.ic_launcher)
        thunderImg.setImageResource(R.drawable.ic_launcher)


        hintsImg.setLayoutParams(layoutParams)
        thunderImg.setLayoutParams(layoutParams)
        knownCards.setLayoutParams(layoutParams)

        addView(hintsImg)
        addView(thunderImg)
        addView(knownCards)

    }
}
