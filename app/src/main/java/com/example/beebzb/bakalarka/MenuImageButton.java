package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.widget.ImageButton;

public class MenuImageButton extends ImageButton {

    private final int GAME_CUSTOM = 0;
    private int color;
    private int gameID;
    private int icon;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuImageButton(Context context, int color, int gameID, int icon ) {
        super(context);
        this.color = color;
        this.gameID = gameID;
        this.icon = icon;

        if (gameID != GAME_CUSTOM) {
            this.setBackground(getResources().getDrawable(R.drawable.circle_shape));
            GradientDrawable bgShape = (GradientDrawable)this.getBackground();
            bgShape.setColor(color);
            this.setBackground(bgShape);
            this.setImageResource(icon);

        }
        else {
            this.setBackground(getResources().getDrawable(R.drawable.customcirclewithicon));
        }

    }



    public int getColor() {
        return color;
    }

    public int getGameID() {
        return gameID;
    }

    public int getIcon() {
        return icon;
    }
}
