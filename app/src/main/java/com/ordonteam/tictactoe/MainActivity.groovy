package com.ordonteam.tictactoe

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import groovy.transform.CompileStatic

@CompileStatic
class MainActivity extends Activity {

    @InjectView(R.id.helloText)
    TextView helloText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        SwissKnife.inject(this)

        helloText.setText('Hello Swiss Knife')
    }
}