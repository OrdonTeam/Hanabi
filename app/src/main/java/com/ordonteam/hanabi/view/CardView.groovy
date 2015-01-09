package com.ordonteam.hanabi.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.ordonteam.hanabi.game.CardColor
import com.ordonteam.hanabi.game.CardValue
import com.ordonteam.hanabi.game.HanabiCard
import groovy.transform.CompileStatic

import java.util.jar.Attributes

@CompileStatic
class CardView extends LinearLayout {
    private TextView textView;
    private Paint paint = new Paint();
    private boolean hasWhiteLine = false;

    CardView(Context context, AttributeSet attrs) {
        super(context, attrs)
        setBackgroundColor(Color.WHITE)

        textView = new BigTextView(context, "?")
        addView(textView)
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas)
        if(hasWhiteLine){
            int times = (int)(getWidth()/5)
            (0..5).each {
                canvas.drawLine(0, it*times, getWidth()-it*times, getHeight(), paint);
                canvas.drawLine(it*times, 0, getWidth(), getHeight()-it*times, paint);
            }
        }
    }

    void setPlayerCard(HanabiCard hanabiCard) {
        setVisibility(VISIBLE)
        setBackgroundColor(hanabiCard.color.color)
        hasWhiteLine = !hanabiCard.isColorKnown
        textView.setText("${hanabiCard.value.value}")
        textView.setTextColor(hanabiCard.isValueKnown ? Color.BLACK : Color.LTGRAY)
    }

    void setUserCard(HanabiCard hanabiCard) {
        setVisibility(VISIBLE)
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

    void setCard(CardColor color, CardValue value) {
        setVisibility(VISIBLE)
        setBackgroundColor(color.color)
        textView.setText("${value.value}")
    }
}
