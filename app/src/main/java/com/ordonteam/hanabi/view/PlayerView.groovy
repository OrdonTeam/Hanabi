package com.ordonteam.hanabi.view

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import com.google.android.gms.common.images.ImageManager
import com.google.android.gms.games.multiplayer.Participant
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.activity.GameActivity
import groovy.transform.CompileDynamic


class PlayerView extends LinearLayout {
    private ImageView playerImage
    private TextView nameFirstLetter
    private Uri uri

    PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs)

        LayoutParams layoutParams = new LayoutParams(context,attrs)
        layoutParams.weight = 1

        playerImage = new ImageView(context)
        playerImage.setImageResource(R.drawable.ic_launcher)
        playerImage.setLayoutParams(layoutParams)
        addView(playerImage)

        nameFirstLetter = new BigTextView(context,'?')
        nameFirstLetter.setLayoutParams(layoutParams)
        addView(nameFirstLetter)


    }

    @CompileDynamic
    void setFirstLetter(Participant participant) {
        String name = participant.getDisplayName()
        nameFirstLetter.setText(name.substring(0,1))


        Uri uri = participant.iconImageUri
        Log.e('setFirstLetter',"$uri")
        if(uri){
            if(this.uri != uri){
                this.uri = uri
                ImageManager.create(context).loadImage(playerImage,uri)//TODO handle size
            }
        } else{
            int nr = Math.abs(name.hashCode()) % 6 + 1
            playerImage.setImageResource(R.drawable."av$nr") // TODO choose random icon based on this letter
        }
    }
}
