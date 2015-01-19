package com.ordonteam.hanabi.view.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ordonteam.hanabi.model.CardColor
import com.ordonteam.hanabi.model.CardValue
import com.ordonteam.hanabi.model.HanabiCard
import groovy.transform.CompileStatic

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
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas)
        if(hasWhiteLine){
            int times = (int)(getWidth()/5)
            (0..5).each {
                canvas.drawLine(0, it*times, getWidth(), getHeight()+it*times, paint);
                canvas.drawLine(it*times, 0, getWidth()+it*times, getHeight(), paint);
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
        textView.setTextColor(Color.BLACK)
    }
}
