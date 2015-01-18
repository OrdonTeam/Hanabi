package com.ordonteam.hanabi.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class BigTextView extends TextView{
    BigTextView(Context context, String defaultText) {
        this(context)
        setText(defaultText)
    }
    BigTextView(Context context) {
        super(context)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 40)
        setGravity(Gravity.CENTER)
    }

    BigTextView(Context context, AttributeSet attrs) {
        super(context, attrs)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 40)
        setGravity(Gravity.CENTER)
    }

    BigTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 40)
        setGravity(Gravity.CENTER)
    }
}
