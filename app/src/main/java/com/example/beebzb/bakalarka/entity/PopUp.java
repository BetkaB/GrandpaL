package com.example.beebzb.bakalarka.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.beebzb.bakalarka.R;
import com.example.beebzb.bakalarka.layout.MyRoundedRect;

import java.util.Random;

public class PopUp {
    private boolean isVisible;
    private Paint backgroundPaint;
    private Paint textPaint;
    private int textX;
    private String message;
    private int x, y, width, height;
    private int lastSeconds;
    private GameHandler gHandler;
    private Context context;
    private String correctAnswer;
    private boolean pausing;
    public final static int CALLBACK = 1;

    private int halfOfTextWidth;
    private int halfOfTextHeight;
    private final int[] correctAnswers = new int[]{R.string.pop_up_correct_answer_1, R.string.pop_up_correct_answer_2, R.string.pop_up_correct_answer_3, R.string.pop_up_correct_answer_4, R.string.pop_up_correct_answer_5};

    public PopUp(Context context, GameHandler gh, int x, int y, int lastSeconds) {
        this.isVisible = false;
        this.x = x;
        this.y = y;
        Log.d("POP UP","canvas width " + x);
        this.width = 0;
        this.height = 0;
        this.lastSeconds = lastSeconds * 1000;
        this.gHandler = gh;
        this.pausing = false;

        this.context = context;

        setRandomCorrectAnswer();
        // text paint settings
        this.textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.textPaint.setColor(Color.WHITE);
        this.textPaint.setTextSize(36);
        Rect bounds = new Rect();
        this.textPaint.getTextBounds(correctAnswer, 0, correctAnswer.length(), bounds);
        this.textX = this.x - (bounds.width()/2);
        halfOfTextWidth = bounds.width()/2;
        halfOfTextHeight = bounds.height()/2;

        // Background paint settings
        this.backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.backgroundPaint.setColor(context.getResources().getColor(R.color.game_activity_pop_up_bg));
        this.backgroundPaint.setStyle(Paint.Style.FILL);



    }

    public void setNewMessage(String msg) {
        this.message = msg;
    }

    public void draw(Canvas canvas) {
        if (!isVisible) return;
        Path path = MyRoundedRect.RoundedRect(new RectF(this.x - this.width / 2, this.y, this.x + this.width / 2, this.y + this.height / 2), 25, 25, true, true, true, true);
        canvas.drawPath(path, backgroundPaint);
        canvas.drawText(correctAnswer.toUpperCase(), this.x - halfOfTextWidth , this.y + halfOfTextHeight + height/4 , this.textPaint);

    }

    private void setRandomCorrectAnswer() {
        Random random = new Random();
        int index = random.nextInt(5);
        correctAnswer = context.getResources().getString(correctAnswers[index]);
    }

    public void show() {
        this.isVisible = true;
    }

    public void hide() {
        this.isVisible = false;
    }

    public void start() {
        Log.e("THREAD-POPUP", "doPopUp() -> start()");
        show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 10 seconds
                hide();
                gHandler.notifyForCallback();
            }
        }, lastSeconds);
    }


    public void handleState(int state) {
        Message msg = gHandler.mHandler.obtainMessage(state);
        msg.sendToTarget();
    }

    public void resize(int w, int h) {
        // Percentage
        int widthPercent = 40;
        int heightPercent = 20;

        // Center - based
        setXY(w / 2, 50);

        this.width = w / 100 * widthPercent;
        this.height = h / 100 * heightPercent;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
