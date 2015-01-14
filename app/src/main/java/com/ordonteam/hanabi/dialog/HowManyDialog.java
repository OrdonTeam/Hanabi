package com.ordonteam.hanabi.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.ordonteam.hanabi.R;

public class HowManyDialog extends AlertDialog.Builder {
    private OnHowManyChooseListener listener;
    private AlertDialog dialog;

    public HowManyDialog(Activity activity) {
        super(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.how_many_dialog, null);
        setView(view);
        view.findViewById(R.id.dialog_how_many_1).setOnClickListener(new HowManyListener(1));
        view.findViewById(R.id.dialog_how_many_2).setOnClickListener(new HowManyListener(2));
        view.findViewById(R.id.dialog_how_many_3).setOnClickListener(new HowManyListener(3));
        view.findViewById(R.id.dialog_how_many_4).setOnClickListener(new HowManyListener(4));
    }

    public HowManyDialog setOnHowManyChooseListener(OnHowManyChooseListener listener){
        this.listener = listener;
        return this;
    }

    @NonNull
    @Override
    public AlertDialog create() {
        dialog = super.create();
        return dialog;
    }

    private class HowManyListener implements View.OnClickListener {
        private int howMany;

        public HowManyListener(int howMany) {
            this.howMany = howMany;
        }

        @Override
        public void onClick(View view) {
            if(dialog!=null)
                dialog.dismiss();
            listener.howManyChoose(howMany);
        }
    }

    public interface OnHowManyChooseListener {
        void howManyChoose(int howMany);
    }
}
