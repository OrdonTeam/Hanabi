package com.ordonteam.hanabi.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import groovy.transform.CompileStatic

@CompileStatic
class ColorNumberDialog extends AlertDialog.Builder{
    ColorNumberDialog(Context context) {
        super(context)
        setTitle("Hint");
        setMessage("You want to give a hint about:");
    }
    ColorNumberDialog setButtonsAction(DialogInterface.OnClickListener positiveListener,DialogInterface.OnClickListener negativeListeners) {
        setPositiveButton("color", positiveListener);
        setNegativeButton("number", negativeListeners);
        return this;
    }
}
