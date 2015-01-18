package com.ordonteam.hanabi.view

import android.content.Context

public interface TurnSubmitter<T> {
    void submitTurn(T turn)
    Context getApplicationContext()
}