package com.ordonteam.hanabi.view

import android.content.Context
import android.util.TypedValue
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
    }
}
