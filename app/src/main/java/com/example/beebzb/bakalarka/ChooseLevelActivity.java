package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;


public class ChooseLevelActivity extends MyActivity {
    private AbsoluteLayout absoluteLayout;
    private int absoluteLayoutHeight;
    private int absoluteLayoutWidth;
    private Resources resources;
    private ImageView middle;
    private Button play;
    private ImageButton left;
    private ImageButton right;

    private int[] gameIcons = new int[]{R.drawable.game1, R.drawable.game2, R.drawable.game3, R.drawable.game4};

    private final int GAME_CUSTOM = 0;
    private final int GAME1 = 1;
    private final int GAME2 = 2;
    private final int GAME3 = 3;
    private final int GAME4 = 4;

    private int chosen_game = 1;
    private int chosen_level = 1;

    private ArrayList<MenuImageButton> buttons = new ArrayList<MenuImageButton>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_level);

        absoluteLayout = (AbsoluteLayout) findViewById(R.id.rel);
        resources = getResources();

        buttons.add( new MenuImageButton(this, resources.getColor(R.color.third_game),GAME_CUSTOM,R.drawable.custom));
        buttons.add( new MenuImageButton(this, resources.getColor(R.color.fourth_game),GAME4,R.drawable.game4));
        buttons.add( new MenuImageButton(this, resources.getColor(R.color.second_game),GAME2,R.drawable.game2));
        buttons.add( new MenuImageButton(this, resources.getColor(R.color.first_game),GAME1,R.drawable.game1));
        buttons.add( new MenuImageButton(this, resources.getColor(R.color.third_game),GAME3,R.drawable.game3));


        ViewTreeObserver observer = absoluteLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                absoluteLayoutHeight = absoluteLayout.getHeight();
                absoluteLayoutWidth = absoluteLayout.getWidth();
                absoluteLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                createButtons();
                setListeners();
            }
        });


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void createButtons(){
        double radius = resources.getDimension(R.dimen.menu_buttons_radius);
        int size = (int) resources.getDimension(R.dimen.menu_buttons_size);
        double centerX = absoluteLayoutWidth/2;
        double centerY = absoluteLayoutHeight/2;

        double deltaAngle = 360 / buttons.size();
        double angle = 0;
        for(int i = 0; i < buttons.size(); i++) {
            double radAngle = Math.toRadians(angle);
            double dAngle = Math.toRadians(-90.0);
            double x = (Math.cos(radAngle + dAngle)) * radius + centerX -size/2;
            double y = (1 - Math.sin(radAngle + dAngle)) * radius + centerY - radius - size/2;
            ImageButton btn = buttons.get(i);
            AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(size, size, (int) x, (int) y);
            btn.setLayoutParams(lp);
            absoluteLayout.addView(btn);
            angle += deltaAngle;
        }

        // middle ImageView
        middle = new ImageButton (this);
        middle.setBackground(getResources().getDrawable(R.drawable.circle_shape));
        GradientDrawable bgShape = (GradientDrawable)middle.getBackground();
        bgShape.setColor(buttons.get(3).getColor());
        middle.setBackground(bgShape);
        AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(size, size, (int) centerX - (size/2), (int) ( centerY - (size/2) - resources.getDimension(R.dimen.middle_button_top_padding))) ;
        middle.setLayoutParams(lp);
        middle.setImageResource(R.drawable.level1);

        absoluteLayout.addView(middle);

        play = new Button(this);
        lp = new AbsoluteLayout.LayoutParams(size, size/3, (int) centerX - (size/2), (int) ( centerY + (size/2) - 25 )) ;
        play.setLayoutParams(lp);
        play.setText(getResources().getString(R.string.play));
        absoluteLayout.addView(play);

        // arrows
        left  = new ImageButton (this);
        left.setBackground(getResources().getDrawable(R.drawable.left));
        right = new ImageButton (this);
        right.setBackground(getResources().getDrawable(R.drawable.right));
        int arrowWidth =  size/7;
        int arrowHeight = size/2;
        lp = new AbsoluteLayout.LayoutParams(arrowWidth, arrowHeight, (int) (centerX - (size/2) - arrowWidth - resources.getDimension(R.dimen.arrows_side_padding)), (int) ( centerY   - arrowHeight/2  - resources.getDimension(R.dimen.middle_button_top_padding))) ;
        left.setLayoutParams(lp);
        left.setEnabled(false);
        left.setBackground(getResources().getDrawable(R.drawable.arrowdisabled));

        absoluteLayout.addView(left);
        lp = new AbsoluteLayout.LayoutParams(arrowWidth, arrowHeight, (int) (centerX + (size/2)  + resources.getDimension(R.dimen.arrows_side_padding)), (int) ( centerY   - arrowHeight/2  - resources.getDimension(R.dimen.middle_button_top_padding))) ;
        right.setLayoutParams(lp);
        absoluteLayout.addView(right);


    }

    public void setListeners(){
        for (MenuImageButton btn : buttons){
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MenuImageButton clickedButton = (MenuImageButton) v;
                        chosen_game = clickedButton.getGameID();
                        if (clickedButton.getGameID() != GAME_CUSTOM){
                            middle.setBackground(getResources().getDrawable(R.drawable.circle_shape));
                            GradientDrawable bgShape = (GradientDrawable)middle.getBackground();
                            bgShape.setColor(clickedButton.getColor());
                            middle.setBackground(bgShape);
                            middle.setImageResource(R.drawable.level1);
                        }
                        else {
                            middle.setBackground(getResources().getDrawable(R.drawable.customcirclebc));
                            middle.setImageResource(R.drawable.game1);
                        }
                        chosen_level = 1;
                        setArrowEnabled(left, true, false);
                        setArrowEnabled(right, false, true);
                    }
                });
            }

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] chosen_array = null;
                chosen_array =  (chosen_game == GAME_CUSTOM) ? gameIcons : Constants.levelIcons;
                if (chosen_level >= chosen_array.length ){
                    setArrowEnabled(right, false, false);
                }
                else {
                    chosen_level++;
                    middle.setImageResource(chosen_array[chosen_level-1]);
                    if (chosen_level == chosen_array.length) {
                        setArrowEnabled(right, false, false);
                    }
                    setArrowEnabled(left, true, true);
                }
            }
        });

       left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] chosen_array = null;
                chosen_array =  (chosen_game == GAME_CUSTOM) ? gameIcons : Constants.levelIcons;
                if (chosen_level <= 1 ){
                    setArrowEnabled(left, true, false);

                }
                else {
                    chosen_level--;
                    middle.setImageResource(chosen_array[chosen_level - 1]);
                    if (chosen_level == 1) {
                        setArrowEnabled(left, true, false);
                        setArrowEnabled(right, false, true);
                    }
                    else {
                        setArrowEnabled(left, true, true);
                    }
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CHOSEN GAME", ""+chosen_game);
                Log.e("CHOSEN LEVEL", ""+chosen_level);
                Intent i = new Intent(getBaseContext(), GameActivity.class);
                i.putExtra("CHOSEN_LEVEL", chosen_level);
                i.putExtra("CHOSEN_GAME", chosen_game);
                startActivity(i);

            }
        });


    }

    private void setArrowEnabled(ImageButton arrow, boolean isLeft, boolean enabled){
        if (enabled) {
            arrow.setEnabled(true);
            if (isLeft){
                arrow.setBackground(getResources().getDrawable(R.drawable.left));}
            else {
                arrow.setBackground(getResources().getDrawable(R.drawable.right));}
        }
        else {
            arrow.setEnabled(false);
            arrow.setBackground(getResources().getDrawable(R.drawable.arrowdisabled));
        }
    }
}

