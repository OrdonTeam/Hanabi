package com.ordonteam.hanabi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.ordonteam.hanabi.game.HanabiCard
import groovy.transform.CompileStatic

import java.util.jar.Attributes

@CompileStatic
class CardView extends LinearLayout {
    int index = 0
    int color = Color.BLACK
    String number = "?"
    private TextView textView;

    CardView(Context context, AttributeSet attrs) {
        super(context, attrs)
        color = Color.WHITE
        number = "?"

        setBackgroundColor(color)
        textView = new TextView(context)

        textView.setText(number)


        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40)
        addView(textView)

    }

    void setColor(int color) {
        this.color = color
        setBackgroundColor(color)
    }

    void setNumber(String number) {
        this.number = number
        textView.setText(number)
    }

    void setCard(HanabiCard hanabiCard) {
        setBackgroundColor(hanabiCard.color.color)
        setAlpha(hanabiCard.isColorKnown ? 1.0f : 0.3f)
        textView.setText("${hanabiCard.value.value}")
        textView.setTextColor(hanabiCard.isValueKnown ? Color.BLACK : Color.GRAY)
    }

    void setUserCard(HanabiCard hanabiCard) {
        if (hanabiCard.isColorKnown)
            setBackgroundColor(hanabiCard.color.color)
        else
            setBackgroundColor(Color.WHITE)

        textView.setTextColor(Color.BLACK)
        if (hanabiCard.isValueKnown)
            textView.setText("${hanabiCard.value.value}")
        else
            textView.setText('?')
    }
}
