package com.ordonteam.tictactoe

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.plus.Plus
import com.ordonteam.inject.InjectActivity
import com.ordonteam.inject.InjectClickListener
import com.ordonteam.inject.InjectContentView
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic

@CompileStatic
@InjectContentView(R.layout.main_layout)
class MainActivity extends InjectActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @InjectView(R.id.helloText)
    TextView helloText

    GoogleApiClient mGoogleApiClient

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        helloText.setText('Hello Robo Guice')
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @InjectClickListener(R.id.signIn)
    void signInClick(View view){
        Log.e('a jednak','działa')
        mGoogleApiClient.connect()
    }

    @Override
    void onConnected(Bundle bundle) {
        Log.e('a jednak','onConnected')
    }

    @Override
    void onConnectionSuspended(int i) {
        Log.e('a jednak','onConnectionSuspended')
    }

    @Override
    void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e('a jednak','onConnectionFailed')
        Log.e('a jednak',connectionResult.toString())
    }
}