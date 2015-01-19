package com.ordonteam.hanabi.view.dialog

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import groovy.transform.CompileStatic

@CompileStatic
class PlayRejectDialog extends AlertDialog.Builder{
    PlayRejectDialog(Context context) {
        super(context)
        setTitle("Action")
        setMessage("What do you want to do?")
    }
    PlayRejectDialog setButtonsAction(DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListeners){
        setPositiveButton("Play the card", positiveListener);
        setNegativeButton("Reject the card", negativeListeners);
        return this
    }
}
