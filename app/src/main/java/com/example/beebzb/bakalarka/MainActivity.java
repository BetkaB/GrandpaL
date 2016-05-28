package com.example.beebzb.bakalarka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageButton;

import java.util.HashMap;


public class MainActivity extends MyActivity {
    private HashMap<Integer, Class> actvities = new HashMap<Integer, Class>();
    SharedPreferences preferences = null;
    public static final String PREFERENCES_NAME = "PREFERENCES";
    public static final String SCORE_GAME1 = "GAME1";
    public static final String SCORE_GAME2 = "GAME2";
    public static final String SCORE_GAME3 = "GAME3";
    public static final String SCORE_GAME4 = "GAME4";
    private final String FIRST_RUN = "isFirstRun";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initActivities();
        boolean isFirstRun = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(FIRST_RUN, true);
        if (isFirstRun) {
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(FIRST_RUN, false).commit();

        }
    }

    private void initActivities() {
        actvities.put(R.id.playBtn, ChooseLevelActivity.class);
        actvities.put(R.id.createBtn, CreateLevelActivity.class);
        actvities.put(R.id.scoreBtn, ScoreActivity.class);

        ImageButton imageButton = (ImageButton) findViewById(R.id.playBtn);
        GradientDrawable bg1 = (GradientDrawable) imageButton.getBackground();
        bg1.setColor(Color.parseColor("#D5BBBB"));

        imageButton = (ImageButton) findViewById(R.id.createBtn);
        bg1 = (GradientDrawable) imageButton.getBackground();
        bg1.setColor(Color.parseColor("#B5A1A1"));

        imageButton = (ImageButton) findViewById(R.id.scoreBtn);
        bg1 = (GradientDrawable) imageButton.getBackground();
        bg1.setColor(Color.parseColor("#988787"));

        imageButton = (ImageButton) findViewById(R.id.leaveBtn);
        bg1 = (GradientDrawable) imageButton.getBackground();
        bg1.setColor(Color.parseColor("#796A6A"));
    }

    public void openActivity(View view) {
        ImageButton clicked = (ImageButton) view;
        Intent intent = new Intent(this, actvities.get(clicked.getId()));
        startActivity(intent);

    }

    public void closeApplication(View view) {
        finish();
    }

}
