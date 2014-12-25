package com.ordonteam.tictactoe

import android.os.Bundle
import android.widget.TextView
import com.ordonteam.inject.InjectActivity
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.main_layout)
class MainActivity extends InjectActivity {

    @InjectView(R.id.helloText)
    TextView helloText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)

        helloText.setText('Hello Robo Guice')
    }
}