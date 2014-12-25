package com.ordonteam.gms

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.plus.Plus
import com.ordonteam.inject.InjectActivity
import com.ordonteam.tictactoe.R
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractGamesActivity extends InjectActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    final static int RC_SIGN_IN = 9001
    final static int RC_SELECT_PLAYERS = 10000
    final static int RC_LOOK_AT_MATCHES = 10001

    GoogleApiClient client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart()
        client.connect()
    }

    @Override
    protected void onStop() {
        super.onStop()
        if (client?.isConnected())
            client.disconnect()
    }

    @Override
    void onConnectionSuspended(int i) {
        Toast.makeText(this, R.string.reconnecting, Toast.LENGTH_LONG).show()
        client.connect()
    }

    @Override
    void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            connectionResult.startResolutionForResult(this, RC_SIGN_IN)
        } else {
            Toast.makeText(this, R.string.loginFailed, Toast.LENGTH_LONG).show()
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode == RESULT_OK) {
                client.connect();
            } else {
                Toast.makeText(this, R.string.loginRequired, Toast.LENGTH_LONG).show()
            }
        }
    }
}
