package com.ordonteam.hanabi.activity;


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.ordonteam.hanabi.R;
import com.ordonteam.inject.InjectActivity;
import com.ordonteam.inject.InjectContentView;
import com.ordonteam.inject.InjectView;

@InjectContentView(R.layout.about)
public class AboutActivity extends InjectActivity {

    @InjectView(R.id.tvAbout)
    private TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String tvAboutContent =
"""OrdonTeam
https://github.com/OrdonTeam
ghostbuster91
https://github.com/ghostbuster91
olabeee
https://github.com/olabeee
pared
https://github.com/pared
wokatorek
https://github.com/wokatorek
daruziek
https://github.com/daruziek""";
        tvAbout.setText(tvAboutContent);

    }
}
