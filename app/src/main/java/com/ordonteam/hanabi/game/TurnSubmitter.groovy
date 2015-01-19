package com.ordonteam.hanabi.game

import android.content.Context

public interface TurnSubmitter<T> {
    void submitTurn(T turn)
    Context getApplicationContext()
}