package com.example.beebzb.bakalarka;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.beebzb.bakalarka.entity.Game;
import com.example.beebzb.bakalarka.layout.MyRoundedRect;

public class MyCanvas extends View implements View.OnTouchListener {
    private Game game;
    private boolean isReady = false, hasUpdatedSize = false;
    private Paint pnt;
    private int canvasWidth, canvasHeight;
    public static final int BOTTOM_ROW_HEIGHT = 200;
    public static int Y_BOTTOM_ROW_CENTER = 0;
    public static int X_BOTTOM_ROW_CENTER = 0;
    private final int PROGRESS_BAR_SIDE_PADDING = 150;
    private final int PROGRESS_BAR_BOTTOM_PADDING = 30;
    private final int PROGRESS_BAR_HEIGHT = 45;
    private Paint paint ;

    public MyCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);;

        drawBottomRow(canvas);
        drawProgressBar(canvas);

        if (!isReady) {
            return;
        }
        game.draw(canvas);

    }

    private void drawProgressBar(Canvas canvas) {
        RectF rectF = new RectF(PROGRESS_BAR_SIDE_PADDING, this.getHeight() - BOTTOM_ROW_HEIGHT - PROGRESS_BAR_BOTTOM_PADDING - PROGRESS_BAR_HEIGHT, this.getWidth() - PROGRESS_BAR_SIDE_PADDING, this.getHeight() - BOTTOM_ROW_HEIGHT - PROGRESS_BAR_BOTTOM_PADDING);
        // progress bar empty
        paint.setColor(getResources().getColor(R.color.progressBarEmpty));
        canvas.drawRoundRect(rectF, 25, 25, paint);
        // progress bar fill progress
        int numberOfTasks = game.getNUMBER_OF_TASKS();
        double progress = game.getProgress();
        paint.setStyle(Paint.Style.FILL);
        RectF rectProgress = new RectF(PROGRESS_BAR_SIDE_PADDING, rectF.top, (float) (PROGRESS_BAR_SIDE_PADDING + (rectF.width() * (progress / 100))), rectF.bottom);
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

    private void drawBottomRow(Canvas canvas) {
        paint.setStrokeWidth(0);
        paint.setColor(getResources().getColor(game.getColor()));
        canvas.drawRect(0, this.getHeight() - BOTTOM_ROW_HEIGHT, this.getWidth(), this.getHeight(), paint);
        Y_BOTTOM_ROW_CENTER = this.getHeight() - (BOTTOM_ROW_HEIGHT/2);
        X_BOTTOM_ROW_CENTER = this.getWidth()/2;
    }


    public void setGame(Game game) {
        this.game = game;
        isReady = true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        game.evaluateTouch(event);

        return true;
    }

    public void updateDelta(double delta) {
        if (game != null) {
            game.updateDelta(delta);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Try for a width based on our minimum
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);

        // Whatever the width ends up being, ask for a height that would let the pie
        // get as big as it can
        int minh = MeasureSpec.getSize(w) + getPaddingBottom() + getPaddingTop();
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);

        canvasWidth = w;
        canvasHeight = h;
        hasUpdatedSize = true;

         /*
        -> notify game object about size change

        if (hasUpdatedSize) {
            gameObject.changeSize(canvasWidth, canvasHeight);
        }
        */


        setMeasuredDimension(w, h);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //canvasWidth = w;
        //canvasHeight = h;
        //hasUpdatedSize = true;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public int getCanvasHeight(){
        return getHeight() - PROGRESS_BAR_BOTTOM_PADDING - PROGRESS_BAR_HEIGHT - BOTTOM_ROW_HEIGHT;
    }




}

