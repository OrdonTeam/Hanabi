package com.ordonteam.hanabi.gms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.games.Games
import com.google.android.gms.plus.Plus
import com.ordonteam.inject.InjectActivity
import com.ordonteam.inject.InjectActivityResult
import com.ordonteam.inject.InjectActivityResultFailed
import com.ordonteam.hanabi.inject.InjectConstants
import com.ordonteam.hanabi.R
import groovy.transform.CompileStatic

@CompileStatic
abstract class AbstractGamesActivity extends InjectActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
    }

    @Override
    protected void onStart() {
        super.onStart()
        if(!client){
            client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();
        }
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
            Log.d('onActivityResult', 'starting')
            connectionResult.startResolutionForResult(this, InjectConstants.RC_SIGN_IN)
        } else {
            Toast.makeText(this, R.string.loginFailed, Toast.LENGTH_LONG).show()
        }
    }

    @InjectActivityResult(requestCode = InjectConstants.RC_SIGN_IN, responseCode = InjectConstants.RESULT_OK)
    void onUserSingIn(int requestCode, int responseCode, Intent intent) {
        Log.d('onActivityResult', 'onUserSingIn')
        client.connect();
    }

    @InjectActivityResultFailed(requestCode = InjectConstants.RC_SIGN_IN)
    void onUserSingInFailed(int requestCode, int responseCode, Intent intent) {
        Log.d('onActivityResult', 'onUserSingInFailed')
        Toast.makeText(this, R.string.loginRequired, Toast.LENGTH_LONG).show()
    }
}
