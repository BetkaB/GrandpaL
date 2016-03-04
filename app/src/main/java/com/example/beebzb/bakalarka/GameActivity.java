package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameActivity extends MyActivity {
    // -------- buttons left menu -----------
    public ImageButton infoBtn;
    public ImageButton helpBtn;
    public ImageButton solutionsBtn;
    public ImageButton leaveBtn;

    private ImageButton[] left_menu_buttons;


    // -------- info row -----------
    public TextView title;
    public TextView text;
    public ImageView level;
    public ImageView helpImView;

    // ---------- level --------------
    private int chosenGame = -1;
    private int chosenLevel = -1;
    private String[] gameInfo;
    private int[] chosenIcons;
    private String gameName;
    private String colorStr;
    private int selectorIndex;

    // -------- constants --------
    private String solutions;
    private String help;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("LEFT_MENU", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // -------- buttons left menu -----------
        infoBtn = (ImageButton) findViewById(R.id.infoBtn);
        helpBtn = (ImageButton) findViewById(R.id.helpBtn);
        solutionsBtn = (ImageButton) findViewById(R.id.solutionsBtn);
        leaveBtn = (ImageButton) findViewById(R.id.leaveBtn);

        // -------- info row -----------
        title = (TextView) findViewById(R.id.infoRowTitle);
        text = (TextView) findViewById(R.id.infoRowText);
        level = (ImageView) findViewById(R.id.infoRowLevel);
        helpImView = (ImageView) findViewById(R.id.helpImView);


        // init
        infoBtn.setSelected(true);
        MyCanvas myCanvas = (MyCanvas) findViewById(R.id.view);
        myCanvas.setGame(new Game(R.color.first_game));
        myCanvas.postInvalidate();
        Bundle extras = getIntent().getExtras();
        chosenGame = extras.getInt("CHOSEN_GAME");
        chosenLevel = extras.getInt("CHOSEN_LEVEL");
        if (chosenGame == 0) {
            chosenIcons = Constants.gameIcons;
            colorStr = Constants.gameColors[chosenLevel - 1];
            selectorIndex = chosenLevel - 1;
            String levelName = getResources().getString(Constants.gameTitles[chosenLevel]);
            gameName = getResources().getString(Constants.gameTitles[chosenGame], levelName);


        } else {
            chosenIcons = Constants.levelIcons;
            colorStr = Constants.gameColors[chosenGame - 1];
            selectorIndex = chosenGame - 1;
            gameName = getResources().getString(Constants.gameTitles[chosenGame]);


        }


        setSelector();

        LinearLayout infoRow = (LinearLayout) findViewById(R.id.infoRow);
        ColorDrawable bg = (ColorDrawable) infoRow.getBackground();
        bg.setColor(Color.parseColor(colorStr));

        Log.e("LEFT_MENU", "GAME " + chosenGame + " LEVEL " + chosenLevel);
        gameInfo = getResources().getStringArray(Constants.gameText[chosenGame]);
        solutions = getResources().getString(R.string.game_activity_solutions);
        help = getResources().getString(R.string.game_activity_help);

        showLevelInfo(gameName, gameInfo[chosenLevel - 1], chosenIcons[chosenLevel - 1]);


        Log.e("LEFT_MENU", "GAME INFO " + gameInfo[1]);
        Log.e("LEFT_MENU", "GAME name " + gameName);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSelector() {
        int selector = Constants.selectors[selectorIndex];
        Drawable leftMenuDrawable = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable2 = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable3 = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable4 = getBaseContext().getResources().getDrawable(selector
        );

        infoBtn.setBackground(leftMenuDrawable);
        helpBtn.setBackground(leftMenuDrawable2);
        solutionsBtn.setBackground(leftMenuDrawable3);
        leaveBtn.setBackground(leftMenuDrawable4);

    }


    public void onButtonClicked(View v) {
        ImageButton clicked = (ImageButton) v;
        int id = clicked.getId();
        clicked.setSelected(true);
        switch (id) {
            case R.id.helpBtn:
                infoBtn.setSelected(false);
                solutionsBtn.setSelected(false);
                showHelp();
                break;
            case R.id.solutionsBtn:
                infoBtn.setSelected(false);
                helpBtn.setSelected(false);
                showSolutionInfo();
                break;
            case R.id.leaveBtn:
                infoBtn.setSelected(false);
                helpBtn.setSelected(false);
                solutionsBtn.setSelected(false);
                onBackPressed();
                break;
            default:
                showLevelInfo(gameName, gameInfo[chosenLevel - 1], chosenIcons[chosenLevel - 1]);
                helpBtn.setSelected(false);
                solutionsBtn.setSelected(false);
                break;
        }

    }

    public void showLevelInfo(String title, String text, int idLevel) {
        this.text.setVisibility(View.VISIBLE);
        this.helpImView.setVisibility(View.GONE);
        this.title.setText(title);
        this.text.setText(text);
        this.level.setImageResource(idLevel);
    }

    public void showSolutionInfo() {
        this.text.setVisibility(View.VISIBLE);
        this.helpImView.setVisibility(View.GONE);
        this.title.setText(solutions);
        this.text.setText(" ");
    }

    public void showHelp() {
        this.title.setText(help);
        this.text.setVisibility(View.GONE);
        this.helpImView.setVisibility(View.VISIBLE);

    }

}
