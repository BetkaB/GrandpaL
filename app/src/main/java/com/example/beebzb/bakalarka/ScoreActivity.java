package com.example.beebzb.bakalarka;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;


public class ScoreActivity extends MyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView score1 = (TextView) findViewById(R.id.gamescore1);
        TextView score2 = (TextView) findViewById(R.id.gamescore2);
        TextView score3 = (TextView) findViewById(R.id.gamescore3);
        TextView score4 = (TextView) findViewById(R.id.gamescore4);

        score1.setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(MainActivity.SCORE_GAME1, 0)));
        score2.setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(MainActivity.SCORE_GAME2, 0)));
        score3.setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(MainActivity.SCORE_GAME3, 0)));
        score4.setText(String.valueOf(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(MainActivity.SCORE_GAME4, 0)));
    }

}

