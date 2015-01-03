package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ordonteam.hanabi.R
import android.widget.LinearLayout.LayoutParams


class GameInfoView extends LinearLayout {

    private ImageView hintsImg
    private ImageView thunderImg
    private CardView knownCards
    private TextView cluesLeft
    private TextView thundersLeft
    GameInfoView(Context context, AttributeSet attrs) {
        super(context, attrs)

        setOrientation(this.HORIZONTAL)

        hintsImg = new ImageView(context)
        thunderImg = new ImageView(context)
        knownCards = new CardView(context,attrs)
        cluesLeft = new TextView(context,attrs)
        thundersLeft = new TextView(context,attrs)

        LayoutParams layoutParams = new LayoutParams(context,attrs)
        layoutParams.weight = 1

        hintsImg.setImageResource(R.drawable.ic_launcher)
        thunderImg.setImageResource(R.drawable.ic_launcher)
        thundersLeft.setText("3")
        thundersLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP,40)
        cluesLeft.setText("8")
        cluesLeft.setTextSize(TypedValue.COMPLEX_UNIT_SP,40)


        hintsImg.setLayoutParams(layoutParams)
        thunderImg.setLayoutParams(layoutParams)
        knownCards.setLayoutParams(layoutParams)
        thundersLeft.setLayoutParams(layoutParams)
        cluesLeft.setLayoutParams(layoutParams)

        addView(hintsImg)
        addView(cluesLeft)
        addView(thunderImg)
        addView(thundersLeft)
        addView(knownCards)



    }
}
