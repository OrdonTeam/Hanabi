package com.ordonteam.hanabi.dialog;

import android.app.Activity;
import android.app.AlertDialog
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.ordonteam.hanabi.R
import com.ordonteam.hanabi.game.CardColor;
import com.ordonteam.hanabi.game.HanabiCard
import com.ordonteam.hanabi.view.BigTextView
import com.ordonteam.hanabi.view.CardView
import groovy.transform.CompileStatic

@CompileStatic
public class RejectedCardsDialog extends AlertDialog.Builder {

    public RejectedCardsDialog(Activity activity, List<HanabiCard> cards) {
        super(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_rejected_cards, null);
        setView(view);
        inflate(cards, CardColor.RED, view, R.id.dialog_rejected_cards_red)
        inflate(cards, CardColor.BLUE, view, R.id.dialog_rejected_cards_blue)
        inflate(cards, CardColor.MAGENTA, view, R.id.dialog_rejected_cards_magenta)
        inflate(cards, CardColor.YELLOW, view, R.id.dialog_rejected_cards_yellow)
        inflate(cards, CardColor.GREEN, view, R.id.dialog_rejected_cards_green)
    }

    private void inflate(List<HanabiCard> cards, CardColor color, View view, int column) {
        List<HanabiCard> redCards = cards.findAll({
            it.color == color
        })
        BigTextView reds = (BigTextView) view.findViewById(column);
        if (redCards) {
            String string = redCards.collect({ HanabiCard it ->
                Integer.valueOf(it.value.value);
            }).sort().join(' ')
            reds.setText(string)
        }else{
            reds.setText('No cards')
        }
    }

}
