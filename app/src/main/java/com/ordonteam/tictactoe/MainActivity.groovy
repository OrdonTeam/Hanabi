package com.ordonteam.tictactoe

import android.app.Activity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        LinearLayout linearLayout = new LinearLayout(this)
        linearLayout.setOrientation(LinearLayout.VERTICAL)

        TextView view = new TextView(this)
        view.setText('Hello Groovy')
        linearLayout.addView(view)

        setContentView(linearLayout)
    }
}