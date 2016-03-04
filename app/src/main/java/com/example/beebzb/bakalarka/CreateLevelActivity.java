package com.example.beebzb.bakalarka;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageButton;

import java.util.HashMap;


public class CreateLevelActivity extends MyActivity {

    private HashMap<Integer, Class > actvities = new HashMap<Integer,Class>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_level);

        actvities.put(R.id.g1, EditorFirstGameActivity.class);
        actvities.put(R.id.g2, EditorFirstGameActivity.class);
        actvities.put(R.id.g3, EditorFirstGameActivity.class);
        actvities.put(R.id.g4, EditorFirstGameActivity.class);


        GridLayout grid = (GridLayout) findViewById(R.id.g1);
        GradientDrawable bg1 = (GradientDrawable)grid.getBackground();
        bg1.setColor(Color.parseColor(Constants.gameColors[0]));

        grid = (GridLayout) findViewById(R.id.g2);
        bg1 = (GradientDrawable)grid.getBackground();
        bg1.setColor(Color.parseColor(Constants.gameColors[1]));

        grid = (GridLayout) findViewById(R.id.g3);
        bg1 = (GradientDrawable)grid.getBackground();
        bg1.setColor(Color.parseColor(Constants.gameColors[2]));

        grid = (GridLayout) findViewById(R.id.g4);
        bg1 = (GradientDrawable)grid.getBackground();
        bg1.setColor(Color.parseColor(Constants.gameColors[3]));
    }

    public void openActivity(View view){
        GridLayout clicked = (GridLayout) view;
        Intent intent = new Intent(this,actvities.get(clicked.getId()));
        startActivity(intent);

    }

}
