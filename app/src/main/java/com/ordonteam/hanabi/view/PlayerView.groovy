package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ordonteam.hanabi.R


class PlayerView extends LinearLayout {
    private ImageView playerImage
    private TextView nameFirstLetter
    PlayerView(Context context) {
        super(context)

        playerImage = new ImageView(context)
        playerImage.setImageResource(R.drawable.ic_launcher)
        addView(playerImage)

        nameFirstLetter = new TextView(context)
        nameFirstLetter.setTextSize(TypedValue.COMPLEX_UNIT_SP,40)
        nameFirstLetter.setText("P")
        addView(nameFirstLetter)
    }
}
