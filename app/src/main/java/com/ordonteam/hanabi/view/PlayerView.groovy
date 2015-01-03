package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import com.ordonteam.hanabi.R


class PlayerView extends LinearLayout {
    private ImageView playerImage
    private TextView nameFirstLetter
    PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs)

        LayoutParams layoutParams = new LayoutParams(context,attrs)
        layoutParams.weight = 1

        playerImage = new ImageView(context)
        playerImage.setImageResource(R.drawable.ic_launcher)
        playerImage.setLayoutParams(layoutParams)
        addView(playerImage)

        nameFirstLetter = new TextView(context)
        nameFirstLetter.setTextSize(TypedValue.COMPLEX_UNIT_SP,40)
        nameFirstLetter.setText("P")
        nameFirstLetter.setLayoutParams(layoutParams)
        addView(nameFirstLetter)
    }
}
