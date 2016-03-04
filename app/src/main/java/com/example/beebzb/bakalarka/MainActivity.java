package com.example.beebzb.bakalarka;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.HashMap;


public class MainActivity extends MyActivity {
    private HashMap<Integer, Class > actvities = new HashMap<Integer,Class>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actvities.put(R.id.playBtn, ChooseLevelActivity.class);
        actvities.put(R.id.createBtn,CreateLevelActivity.class);
        actvities.put(R.id.scoreBtn, ScoreActivity.class);

        ImageButton imageButton = (ImageButton) findViewById(R.id.playBtn);
        GradientDrawable bg1 = (GradientDrawable)imageButton.getBackground();
        bg1.setColor(Color.parseColor("#D5BBBB"));

        imageButton = (ImageButton) findViewById(R.id.createBtn);
        bg1 = (GradientDrawable)imageButton.getBackground();
        bg1.setColor(Color.parseColor("#B5A1A1"));

        imageButton = (ImageButton) findViewById(R.id.scoreBtn);
        bg1 = (GradientDrawable)imageButton.getBackground();
        bg1.setColor(Color.parseColor("#988787"));

        imageButton = (ImageButton) findViewById(R.id.leaveBtn);
        bg1 = (GradientDrawable)imageButton.getBackground();
        bg1.setColor(Color.parseColor("#796A6A"));
    }

    public void openActivity(View view){
        ImageButton clicked = (ImageButton) view;
        Intent intent = new Intent(this,actvities.get(clicked.getId()));
        startActivity(intent);

    }
    public void closeApplication(View view){
        finish();
    }

}
