package com.ordonteam.hanabi.view.row

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.gms.common.images.ImageManager
import com.google.android.gms.games.multiplayer.Participant
import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.view.common.BigTextView
import groovy.transform.CompileDynamic


class PlayerView extends LinearLayout {
    private ImageView playerImage
    private TextView nameFirstLetter
    private Uri uri

    PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs)

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(context,attrs)
        layoutParams.weight = 1
        layoutParams.width = 0
        layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT
        layoutParams.gravity = Gravity.CENTER

        playerImage = new ImageView(context)
        playerImage.setImageResource(R.drawable.ic_launcher)
        playerImage.setLayoutParams(layoutParams)
        addView(playerImage)

        nameFirstLetter = new BigTextView(context,'?')
        nameFirstLetter.setLayoutParams(layoutParams)
        addView(nameFirstLetter)
    }

    void setPlayerInfo(Participant participant) {
        if(!participant)
            return
        String name = participant.getDisplayName()
        nameFirstLetter.setText(name.substring(0,1))

        Uri uri = participant.hiResImageUri
        if(uri){
            if(this.uri != uri){
                this.uri = uri
                ImageManager.create(context).loadImage(playerImage,uri)//TODO handle size
            }
        } else{
            setLetterBasedOnString(name)
        }
    }

    @CompileDynamic
    private void setLetterBasedOnString(String name) {
        int nr = Math.abs(name.hashCode()) % 6 + 1
        playerImage.setImageResource(R.drawable."av$nr")
    }

    void setLetterColor(int color) {
        nameFirstLetter.setTextColor(color)
    }
}
