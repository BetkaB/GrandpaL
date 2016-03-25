package com.example.beebzb.bakalarka.entity;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

import com.example.beebzb.bakalarka.GameActivity;
import com.example.beebzb.bakalarka.MainActivity;
import com.example.beebzb.bakalarka.MyCanvas;
import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class GameHandler {

    private int selected = -1;
    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCircles;
    public static Context context;
    private Game game;
    public static MyCanvas canvas;
    private GameActivity activity;
    private ArrayList<Circle> bottomRowCircles;

    private boolean paused;
    private PopUp popUpWidget;
    public static Handler mHandler;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameHandler(Context context, Game game, MyCanvas myCanvas, GameActivity activity) {
        this.game = game;
        this.activity = activity;
        this.context = context;
        this.canvas = myCanvas;

        circles = new ArrayList<>();
        staticCircles = new ArrayList<>();

        // Pop Up settings
        this.popUpWidget = new PopUp(context, this, myCanvas.canvasWidth, myCanvas.canvasHeight, 2);

        // Handler GUI

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PopUp.CALLBACK:
                        Log.d("THREAD-POPUP", "Obtained message!");
                        popUpWidget.start();
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };


    }

    private void drawBottomRowCircles(Canvas canvas) {
        if (circles.size() == 0) {
            Log.e("BOTTOM_ROW", "null");
            circles = getBottomRowCircles();
        }
        for (Circle cr : circles) {
            cr.draw(canvas);
        }
    }

    private int findCircle(int x, int y) {
        for (int i = 0; i < circles.size(); i++) {
            Circle temp = circles.get(i);

            float dx = (x - temp.x);
            float dy = (y - temp.y);

            if ((dx * dx + dy * dy) <= (temp.radius * temp.radius)) {
                return i;
            }
        }
        return -1;
    }

    public void draw(Canvas canvas) {

        for (Circle scr : staticCircles) {
            scr.draw(canvas);
        }

        if (game.getCurrentTask() != null) {
            game.getCurrentTask().draw(canvas);
            staticCircles = game.getCurrentTask().getStaticCircles();
            for (Circle circle : staticCircles) {
                circle.draw(canvas);
            }

        }
        drawBottomRowCircles(canvas);
        this.popUpWidget.draw(canvas);

    }

    private void updateScore() {
        String key = "";
        switch (game.getChosenGame()) {
            case 1:
                key = MainActivity.SCORE_GAME1;
                break;
            case 2:
                key = MainActivity.SCORE_GAME2;
                break;
            case 3:
                key = MainActivity.SCORE_GAME3;
                break;
            case 4:
                key = MainActivity.SCORE_GAME4;
                break;
        }
        int score = PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
        score += 5;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, score).commit();
    }

    public void evaluateTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.selected = findCircle(((int) event.getX()), ((int) event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                if (this.selected != -1) {
                    circles.get(this.selected).moveDrag(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:

                if (this.selected != -1) {
                    circles.get(this.selected).clearDXY();
                }
                this.selected = -1;
                break;
        }
    }

    public void updateDelta(double delta) {
        // Drag engine
        for (Circle temp : circles) {
            int sumForceX = 0;
            int sumForceY = 0;
            float vX, vY, dX, dY;
            double vR;

            for (Circle staticCircle : staticCircles) {
                int idx = staticCircles.indexOf(staticCircle);

                vX = staticCircle.x - temp.x;
                vY = staticCircle.y - temp.y;

                vR = (temp.radius + staticCircle.radius) - Math.sqrt(vX * vX + vY * vY);

                // Intersection behaviour
                if (vR >= 0) {
                    dX = vX / 2;
                    dY = vY / 2;
                    sumForceX += dX;
                    sumForceY += dY;

                    // set operation of set movable circle
                    // currentTask.setOperation();
                    // temp

                    if (staticCircle.getOperation() ==  Operation.EMPTY){
                        //Log.e("SOLVER","we are guessing operation");
                        game.getCurrentTask().setOperation(temp.getOperation());
                    }
                    else {
                        //Log.e("SOLVER","we are guessing animal");
                        game.getCurrentTask().setEmptyAnimal(temp.getAnimal());
                    }

                    boolean result = game.isCurrentTaskSolved();
                    // backtrack vymazat a nechat volne miesto

                    if (result && !paused) {
                        game.incrementCompletedTasks();

                        // Pause game + Pop Up + checkGame()
                        doPopUp();

                    }

                    // Handle enter
                    if (temp.getValue() == idx) {
                        temp.setValue(idx);
                    }
                } else {
                    if (temp.getValue() == idx) {
                        temp.setValue(Circle.LEAVE);

                    }
                }
            }
            temp.updateDragForce(-sumForceX, -sumForceY, delta);
        }
    }

    private void doPopUp() {
        this.paused = true;
        //(new Thread(this.popUpWidget)).start();
        Log.e("THREAD-POPUP", "doPopUp()");
        popUpWidget.handleState(popUpWidget.CALLBACK);
    }

    public ArrayList<Circle> getBottomRowCircles() {
        ArrayList<Circle> circles = new ArrayList<Circle>();
        switch (game.getChosenGame()) {
            case 1:
                circles = getFirstGameBottomRow(game.getChosenLevel());
                break;
            case 2:
            case 4:
                circles = getSecondThirdGameBottomRow(game.getChosenLevel());
                break;
        }
        return circles;
    }


    public ArrayList<Circle> getFirstGameBottomRow(int chosenLevel) {
        int y = canvas.Y_BOTTOM_ROW_CENTER;
        int x = canvas.X_BOTTOM_ROW_CENTER;
        int padding = 10;
        ArrayList<Circle> circles = new ArrayList<Circle>();
        final int radius = 60;
        int total_width;
        int startX;
        switch (chosenLevel) {
            case 1:
                total_width = 2 * (2 * radius + padding);
                startX = x - (total_width / 2) + (radius / 2);
                circles.add(new Circle(startX, y, radius, false, context, null, Operation.EQUAL));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, radius, false, context, null, Operation.NOT_EQUAL));
                break;
            case 2:
            case 3:
                total_width = 3 * (2 * radius + padding);
                startX = x - (total_width / 2) + (radius / 2);
                circles.add(new Circle(startX, y, radius, false, context, null, Operation.EQUAL));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, radius, false, context, null, Operation.GREATER_THAN));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, radius, false, context, null, Operation.LESS_THAN));
                break;

        }
        return circles;
    }

    public ArrayList<Circle> getSecondThirdGameBottomRow(int chosenLevel) {
        int y = canvas.Y_BOTTOM_ROW_CENTER;
        int x = canvas.X_BOTTOM_ROW_CENTER;
        int padding = 10;
        ArrayList<Circle> circles = new ArrayList<Circle>();
        final int radius = 60;
        int total_width;
        int startX;
        total_width = 6 * (2*radius + padding);
        startX = x - (total_width / 2) + (radius / 2);
        circles.add(new Circle(startX, y, radius, false, context, Animal.MOUSE, null));
        startX += (2 * radius) + padding;

        circles.add(new Circle(startX, y, radius, false, context, Animal.CAT, null));
        startX += (2 * radius) + padding;

        circles.add(new Circle(startX, y, radius, false, context, Animal.GOOSE, null));
        startX += (2 * radius) + padding;

        circles.add(new Circle(startX, y, radius, false, context, Animal.DOG, null));
        startX += (2 * radius) + padding;

        circles.add(new Circle(startX, y, radius, false, context, Animal.GOAT, null));
        startX += (2 * radius) + padding;

        circles.add(new Circle(startX, y, radius, false, context, Animal.RAM, null));
        startX += (2 * radius) + padding;

        switch (chosenLevel) {
            case 3:
                circles.add(new Circle(startX, y, radius, false, context, Animal.COW, null));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, radius, false, context, Animal.HORSE, null));
                break;
        }
        return circles;
    }

    public double getGameProgress() {
        return game.getProgress();
    }

    public int getGameColor() {
        return game.getColor();
    }

    public int getNUMBER_OF_TASKS() {
        return game.getNUMBER_OF_TASKS();
    }

    public void notifyForCallback() {

        Log.e("THREAD-POPUP", "notified!()");
        // Callback for handler!
        checkGame();

    }

    public void checkGame() {
        Log.e("THREAD-POPUP", "checkGame!()");

        // update number of solutions if clicked
        // init bottom circles - works when motion UP
        circles = getBottomRowCircles();

        if (game.areAllTasksCompleted()) {
            updateScore();
            activity.gameOn = false;
            activity.finish();
        } else {
            game.getNextTask();
        }

        // Unblock
        this.paused = false;
    }

    public void resizeWidget(int w, int h) {
        popUpWidget.resize(w, h);
    }


}
