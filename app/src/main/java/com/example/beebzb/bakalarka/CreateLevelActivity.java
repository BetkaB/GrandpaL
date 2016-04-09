package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;


public class CreateLevelActivity extends MyActivity {

    private HashMap<Integer, Integer > activities = new HashMap<Integer, Integer>();

    private final int THREE_LEVELS_DONE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_level);

        activities.put(R.id.g1, 1);
        activities.put(R.id.g2, 2);
        activities.put(R.id.g3, 3);
        activities.put(R.id.g4, 4);


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
        int chosenGame = activities.get(clicked.getId());
        String key = ChooseLevelActivity.getKeyByChosenGame(chosenGame);
         int gameRes = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(key, ChooseLevelActivity.PROGRESS_DEFAULT);
        if (gameRes == THREE_LEVELS_DONE){
            Intent intent = new Intent(this, EditorActivity.class );
            intent.putExtra("CHOSEN_GAME", chosenGame);
            startActivity(intent);
        }
        else {
            Resources res = getBaseContext().getResources();
           new MyDialog(CreateLevelActivity.this,res.getString(R.string.pre_editor_dialog_cannot_create_task_title), res.getString(R.string.pre_editor_dialog_cannot_create_task_info), false);
        }
    }

}
