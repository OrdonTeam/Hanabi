package com.ordonteam.hanabi.activity

import android.os.Bundle
import android.text.util.Linkify
import android.widget.ImageView
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ordonteam.hanabi.R;
import com.ordonteam.inject.InjectActivity;
import com.ordonteam.inject.InjectContentView;
import com.ordonteam.inject.InjectView
import groovy.transform.CompileStatic;

@CompileStatic
@InjectContentView(R.layout.about)
class AboutActivity extends InjectActivity {

    @InjectView(R.id.about_content)
    LinearLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        add('OrdonTeam')
        add('ghostbuster91')
        add('olabeee')
        add('pared')
        add('wokatorek')
        add('daruziek')

    }

    void add(String login) {
        LinearLayout user = new LinearLayout(this)
        user.setOrientation(LinearLayout.HORIZONTAL)
        ImageView icon = new ImageView(this)
        icon.setImageResource(R.drawable.ic_launcher)
        user.addView(icon)

        LinearLayout about = new LinearLayout(this)
        about.setOrientation(LinearLayout.VERTICAL)

        TextView loginView = new TextView(this)
        loginView.setText(login)
        about.addView(loginView)
        TextView urlView = new TextView(this)
        urlView.setAutoLinkMask(Linkify.ALL)
        urlView.setText("https://github.com/$login")
        about.addView(urlView)

        user.addView(about)
        content.addView(user)
    }
}
