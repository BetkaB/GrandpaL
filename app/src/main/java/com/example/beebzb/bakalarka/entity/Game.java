package com.example.beebzb.bakalarka.entity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;

import com.example.beebzb.bakalarka.MainActivity;
import com.example.beebzb.bakalarka.MyCanvas;
import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Game {
    private int color;
    private final int NUMBER_OF_TASKS = 3;
    private int tasks_completed = 0;

    private int selected = -1;
    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCircles;
    public static Context context;
    private int chosenGame;
    private int chosenLevel;
    public static MyCanvas canvas;
    private Task currentTask;
    private Solver solver = new Solver();
    private Activity activity;

    private ArrayList<Task> tasks = new ArrayList<Task>();

    private ArrayList<Circle> bottomRowCircles;

    public int getColor() {
        return color;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Game(int color, Context context, int chosenGame, int chosenLevel, MyCanvas myCanvas, Activity activity) {
        this.chosenGame = chosenGame;
        this.chosenLevel = chosenLevel;
        this.activity = activity;
        this.context = context;
        this.color = color;
        this.canvas = myCanvas;

        circles = new ArrayList<>();
        tasks = new Generator().generateTasksForFirstGame(NUMBER_OF_TASKS, chosenLevel);

        staticCircles = new ArrayList<>();

    }

    private Task getCurrentTask() {
        for (Task task : tasks) {
            if (!task.isCompleted()) {
                return task;
            }
        }
        return null;
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

    public int getNUMBER_OF_TASKS() {
        return NUMBER_OF_TASKS;
    }

    public double getProgress() {
        return 100 / NUMBER_OF_TASKS * tasks_completed;
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

        if (tasks_completed != NUMBER_OF_TASKS) {
            currentTask = getCurrentTask();
            if (currentTask != null) {
                currentTask.draw(canvas);
                staticCircles = currentTask.getStaticCircles();
                for (Circle circle : staticCircles) {
                    circle.draw(canvas);
                }
            }
        }
        drawBottomRowCircles(canvas);


    }

    public void getNextTask() {
        if (currentTask != null) {
            if (currentTask.isCompleted()) {
                tasks_completed++;
                if (tasks_completed == NUMBER_OF_TASKS) {
                    activity.onBackPressed();
                   // return;
                }
                currentTask = getCurrentTask();
                if (currentTask == null) {

                }
                updateScore();


            }
        }
    }

    private void updateScore() {
        String key = "";
        switch (chosenGame) {
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

                    currentTask.setOperation(temp.getOperation());
                    boolean result = solver.isSolved(currentTask);
                    if (result) {
                        currentTask.setCompleted(true);

                        getNextTask();
                    }
                    Log.e("TASK", "---1----");

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

    public ArrayList<Circle> getBottomRowCircles() {
        ArrayList<Circle> circles = new ArrayList<Circle>();
        switch (chosenGame) {
            case 1:
                circles = getFirstGameBottomRow(chosenLevel);
                break;
            case 2:
            case 4:
                circles = getSecondThirdGameBottomRow(chosenLevel);
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
        total_width = 8 * (radius + padding);
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
}
