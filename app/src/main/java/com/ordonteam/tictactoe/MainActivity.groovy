package com.ordonteam.tictactoe

import android.os.Bundle
import android.widget.TextView
import groovy.transform.CompileStatic
import roboguice.activity.RoboActivity
import roboguice.inject.ContentView
import roboguice.inject.InjectView

@CompileStatic
@ContentView(R.layout.main_layout)
class MainActivity extends RoboActivity {

    @InjectView(R.id.helloText)
    TextView helloText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)

        helloText.setText('Hello Robo Guice')
    }
}