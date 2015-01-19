package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.view.common.BigTextView
import groovy.transform.CompileStatic

@CompileStatic
class DeckView  extends LinearLayout {
    private TextView textView;

    DeckView(Context context, AttributeSet attrs) {
        super(context, attrs)
        setBackground( getResources().getDrawable(R.drawable.ic_launcher))

        textView = new BigTextView(context, "50")
        addView(textView)
    }

    void setRemainingCardsLeft(int count){
        textView.setText("$count")
    }
}
