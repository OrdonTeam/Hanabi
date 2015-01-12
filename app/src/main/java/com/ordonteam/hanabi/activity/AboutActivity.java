package com.ordonteam.hanabi.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ordonteam.hanabi.R;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView tvAbout=(TextView) findViewById(R.id.tvAbout);
        String tvAboutContent = "OrdonTeam\nhttps://github.com/OrdonTeam\n" +
                "ghostbuster91\nhttps://github.com/ghostbuster91\n" +
                "olabeee\nhttps://github.com/olabeee\n" +
                "pared\nhttps://github.com/pared\n" +
                "wokatorek\nhttps://github.com/wokatorek\n" +
                "daruziek\nhttps://github.com/daruziek";
        tvAbout.setText(tvAboutContent);
        Button bClose = (Button) findViewById(R.id.bClose);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
