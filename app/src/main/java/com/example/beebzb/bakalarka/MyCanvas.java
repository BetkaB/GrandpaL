package com.example.beebzb.bakalarka;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

public class MyCanvas extends View {
    private Game game;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        final int BOTTOM_ROW_HEIGHT = 200;
        final int PROGRESS_BAR_SIDE_PADDING = 150;
        final int PROGRESS_BAR_BOTTOM_PADDING = 30;
        final int PROGRESS_BAR_HEIGHT = 45;
        // draw bottom row
        paint.setStrokeWidth(0);
        paint.setColor(getResources().getColor(game.getColor()));
        canvas.drawRect(0, this.getHeight() - BOTTOM_ROW_HEIGHT, this.getWidth(), this.getHeight(), paint);
        // draw progress bar
        RectF rectF = new RectF(PROGRESS_BAR_SIDE_PADDING, this.getHeight() - BOTTOM_ROW_HEIGHT - PROGRESS_BAR_BOTTOM_PADDING - PROGRESS_BAR_HEIGHT,
                this.getWidth() - PROGRESS_BAR_SIDE_PADDING,
                this.getHeight() - BOTTOM_ROW_HEIGHT - PROGRESS_BAR_BOTTOM_PADDING);
        // progress bar empty
        paint.setColor(getResources().getColor(R.color.progressBarEmpty));
        canvas.drawRoundRect(rectF, 25, 25, paint);
        // progress bar fill progress
        int numberOfTasks = game.getNUMBER_OF_TASKS();
        double progress = game.getProgress();
        Log.e("CANVAS", "progress " + progress);
        paint.setStyle(Paint.Style.FILL);
        RectF rectProgress = new RectF(PROGRESS_BAR_SIDE_PADDING, rectF.top,
                (float) (PROGRESS_BAR_SIDE_PADDING + (rectF.width() * (progress / 100))),
                rectF.bottom);
        paint.setColor(getResources().getColor(game.getColor()));
        if (progress != 100.0) {

            Path path = MyRoundedRect.RoundedRect(rectProgress, 25, 25, true, false, false, true);
            canvas.drawPath(path, paint);
        } else {
            canvas.drawRoundRect(rectProgress, 25, 25, paint);
        }

        // progress bar lines and border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(getResources().getColor(R.color.progressBarStroke));
        for (int i = 1; i < numberOfTasks; i++) {
            float x = (float) (PROGRESS_BAR_SIDE_PADDING + ((rectF.width() / numberOfTasks) * i));
            canvas.drawLine(x, rectF.top, x, rectF.bottom, paint);
        }
        canvas.drawRoundRect(rectF, 25, 25, paint);


    }

    public void run() {

    }

    public void setGame(Game game) {
        this.game = game;
    }
}

