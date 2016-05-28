package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beebzb.bakalarka.entity.Constants;
import com.example.beebzb.bakalarka.fragments.EditorGame1Fragment;
import com.example.beebzb.bakalarka.fragments.EditorGame2Fragment;
import com.example.beebzb.bakalarka.fragments.EditorGame3Fragment;
import com.example.beebzb.bakalarka.fragments.EditorGame4Fragment;

public class EditorActivity extends MyActivity {

    // -------- buttons left menu -----------
    public ImageButton infoBtn;
    public ImageButton helpBtn;
    public ImageButton leaveBtn;
    public ImageButton saveBtn;

    // -------- info row -----------
    public TextView title;
    public TextView text;
    public ImageView helpImageView;
    public ImageView gameIcon;

    // ---------- level --------------
    private String gameInfo;
    private String gameName;
    private String colorStr;
    private int selectorIndex;
    private int chosenGame;

    // -------- fragments ---------
    EditorGame1Fragment editorGame1Fragment;
    EditorGame2Fragment editorGame2Fragment;
    EditorGame3Fragment editorGame3Fragment;
    EditorGame4Fragment editorGame4Fragment;

    public static final String DEFAULT_TASK = "default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // -------- buttons left menu -----------
        infoBtn = (ImageButton) findViewById(R.id.infoBtn);
        helpBtn = (ImageButton) findViewById(R.id.helpBtn);
        leaveBtn = (ImageButton) findViewById(R.id.leaveBtn);
        saveBtn = (ImageButton) findViewById(R.id.saveBtn);

        // -------- info row -----------
        title = (TextView) findViewById(R.id.infoRowTitle);
        text = (TextView) findViewById(R.id.infoRowText);
        helpImageView = (ImageView) findViewById(R.id.helpImView);
        gameIcon = (ImageView) findViewById(R.id.gameIcon);

        // init
        Bundle extras = getIntent().getExtras();
        chosenGame = extras.getInt("CHOSEN_GAME");
        infoBtn.setSelected(true);

        // init constants
        colorStr = Constants.gameColors[chosenGame - 1];
        selectorIndex = chosenGame - 1;
        gameName = getResources().getString(Constants.editorTitle[chosenGame - 1]);

        setSelector();

        LinearLayout infoRow = (LinearLayout) findViewById(R.id.infoRow);
        ColorDrawable bg = (ColorDrawable) infoRow.getBackground();
        bg.setColor(Color.parseColor(colorStr));

        gameInfo = getResources().getString(Constants.editorText[chosenGame - 1]);

        showLevelInfo(gameName, gameInfo, Constants.gameIcons[chosenGame - 1]);

        // init fragments
        editorGame1Fragment = (EditorGame1Fragment) getFragmentManager().findFragmentById(R.id.editorGame1);
        editorGame2Fragment = (EditorGame2Fragment) getFragmentManager().findFragmentById(R.id.editorGame2);
        editorGame3Fragment = (EditorGame3Fragment) getFragmentManager().findFragmentById(R.id.editorGame3);
        editorGame4Fragment = (EditorGame4Fragment) getFragmentManager().findFragmentById(R.id.editorGame4);


        displayFragment();
    }

    private void displayFragment() {
        FragmentManager fm = getFragmentManager();
        switch (chosenGame) {
            case 1:
                fm.beginTransaction()
                        .show(editorGame1Fragment)
                        .hide(editorGame2Fragment)
                        .hide(editorGame3Fragment)
                        .hide(editorGame4Fragment)
                        .commit();
                break;
            case 2:
                fm.beginTransaction()
                        .show(editorGame2Fragment)
                        .hide(editorGame1Fragment)
                        .hide(editorGame3Fragment)
                        .hide(editorGame4Fragment)
                        .commit();
                break;
            case 3:
                fm.beginTransaction()
                        .show(editorGame3Fragment)
                        .hide(editorGame1Fragment)
                        .hide(editorGame2Fragment)
                        .hide(editorGame4Fragment)
                        .commit();
                break;
            case 4:
                fm.beginTransaction()
                        .show(editorGame4Fragment)
                        .hide(editorGame1Fragment)
                        .hide(editorGame2Fragment)
                        .hide(editorGame3Fragment)
                        .commit();
                break;

        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setSelector() {
        int selector = Constants.selectors[selectorIndex];
        Drawable leftMenuDrawable = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable2 = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable3 = getBaseContext().getResources().getDrawable(selector);
        Drawable leftMenuDrawable4 = getBaseContext().getResources().getDrawable(selector);

        infoBtn.setBackground(leftMenuDrawable);
        helpBtn.setBackground(leftMenuDrawable2);
        leaveBtn.setBackground(leftMenuDrawable4);
        saveBtn.setBackground(leftMenuDrawable3);

    }


    public void onButtonClicked(View v) {
        ImageButton clicked = (ImageButton) v;
        int id = clicked.getId();
        clicked.setSelected(true);
        switch (id) {
            case R.id.helpBtn:
                infoBtn.setSelected(false);
                showHelp();
                break;
            case R.id.leaveBtn:
                infoBtn.setSelected(false);
                helpBtn.setSelected(false);
                onBackPressed();
                break;
            default:
                showLevelInfo(gameName, gameInfo, Constants.gameIcons[chosenGame - 1]);
                helpBtn.setSelected(false);
                break;
        }

    }

    public void showLevelInfo(String title, String text, int gameIcon) {
        this.title.setVisibility(View.VISIBLE);
        this.text.setVisibility(View.VISIBLE);
        this.helpImageView.setVisibility(View.GONE);
        this.title.setText(title);
        this.text.setText(text);
        this.gameIcon.setImageResource(gameIcon);
    }


    public void showHelp() {
        title.setVisibility(View.GONE);
        this.text.setVisibility(View.GONE);
        this.helpImageView.setVisibility(View.VISIBLE);
    }

    public void save(View view) {
        switch (chosenGame) {
            case 1:
                editorGame1Fragment.onSaveClicked();
                break;
            case 2:
                editorGame2Fragment.onSaveClicked();
                break;
            case 3:
                editorGame3Fragment.onSaveClicked();
                break;
            case 4:
                editorGame4Fragment.onSaveClicked();
                break;
        }


    }


}