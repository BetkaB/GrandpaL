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

import com.example.beebzb.bakalarka.ChooseLevelActivity;
import com.example.beebzb.bakalarka.GameActivity;
import com.example.beebzb.bakalarka.MainActivity;
import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class GameHandler {

    private int selected = -1;
    private boolean isMoving = false;
    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCircles;
    public static Context context;
    public static Game game;
    public static MyCanvas canvas;
    private GameActivity activity;
    private ArrayList<Circle> bottomRowCircles;

    private int lastTouchX, lastTouchY;

    private boolean paused;
    private PopUp popUpWidget;
    public static Handler mHandler;


    // ------- 3. game

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameHandler(Context context, final Game game, MyCanvas myCanvas, GameActivity activity) {
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
            Game refGame = game;

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PopUp.CALLBACK:
                        Log.d("THREAD-POPUP", "Obtained message!");
                        popUpWidget.start();
                        break;
                    case MyCanvas.INIT_SIZE_CANVAS:
                        Log.d("CANVAS", "INIT_SIZE_CANVAS!");
                        circles = getBottomRowCircles();
                        refGame.getCurrentTask().resize(canvas.canvasWidth, canvas.canvasHeight);
                        break;
                    default:
                        super.handleMessage(msg);
                }
            }
        };

        Log.d("DEBUG_3_GAME", String.valueOf(game.getChosenGame()));

    }


    private void drawBottomRowCircles(Canvas canvas) {
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
        if (game.getCurrentTask() != null) {
            game.getCurrentTask().draw(canvas);
            // Referencia
            staticCircles = game.getCurrentTask().getStaticCircles();


            for (Circle circle : staticCircles) {
                circle.draw(canvas);
            }
            drawBottomRowCircles(canvas);
            this.popUpWidget.draw(canvas);

        }
    }

    private void updateScore() {
        String key = "";
        String keyProgress = "";
        switch (game.getChosenGame()) {
            case 1:
                key = MainActivity.SCORE_GAME1;
                keyProgress = ChooseLevelActivity.PROGRESS_GAME_1;
                break;
            case 2:
                key = MainActivity.SCORE_GAME2;
                keyProgress = ChooseLevelActivity.PROGRESS_GAME_2;
                break;
            case 3:
                key = MainActivity.SCORE_GAME3;
                keyProgress = ChooseLevelActivity.PROGRESS_GAME_3;
                break;
            case 4:
                key = MainActivity.SCORE_GAME4;
                keyProgress = ChooseLevelActivity.PROGRESS_GAME_4;
                break;
        }
        int score = PreferenceManager.getDefaultSharedPreferences(context).getInt(key, 0);
        score += 5;
        PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(key, score).commit();
        int levelDone = PreferenceManager.getDefaultSharedPreferences(context).getInt(keyProgress, ChooseLevelActivity.PROGRESS_DEFAULT);
        int newLevelDone = game.getChosenLevel() + 1;
        if (newLevelDone > levelDone){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(keyProgress, newLevelDone).commit();
        }


    }


    public void evaluateTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.selected = findCircle(((int) event.getX()), ((int) event.getY()));
                this.lastTouchX = (int) event.getX();
                this.lastTouchY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // Move(drag to touch)
                this.isMoving = true;
                if (this.selected != -1) {
                    circles.get(this.selected).moveDrag(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                // Clear touch-dragging
                this.isMoving = false;
                if (this.selected != -1) {
                    circles.get(this.selected).clearDXY();
                }
                // Deselect
                this.selected = -1;
                break;
        }
    }

    public void updateDelta(double delta) {
        // Drag engine
        for (Circle temp : circles) {
            int cidx = circles.indexOf(temp);
            int sumForceX = 0;
            int sumForceY = 0;
            float vX, vY, dX, dY, maxdX, maxdY;
            double vR, maxVR;

            // Maximal forces init
            maxVR = -1;
            maxdX = 0;
            maxdY = 0;

            for (Circle staticCircle : staticCircles) {
                int idx = staticCircles.indexOf(staticCircle);
                boolean occupiedReversedForce = false;

                vX = staticCircle.x - temp.x;
                vY = staticCircle.y - temp.y;
                vR = (temp.radius + staticCircle.radius) - Math.sqrt(vX * vX + vY * vY);

                // Intersection behaviour
                if (!isMoving) {
                    if (vR >= 0) {
                        dX = vX / 2;
                        dY = vY / 2;

                        // Remember max forces
                        if (vR >= maxVR) {
                            maxVR = vR;
                            maxdX = dX;
                            maxdY = dY;
                        }

                        //if ((Math.round(dX) != 0) || (Math.round(dY) != 0)) isDraggedEqually = true;

                        sumForceX += dX;
                        sumForceY += dY;

                        // set operation of set movable circle

                        // New Handle Enter
                        if (staticCircle.isOccupied() && (staticCircle.getValue() != cidx)) {
                            // Putting another circle
                            // occupied Reversed Force
                            sumForceX = getDXfromLastTouch((int) temp.x);
                            sumForceY = getDYfromLastTouch((int) temp.y);
                            break;
                        } else if (!staticCircle.isOccupied()) {
                            staticCircle.setValue(cidx);
                            temp.setValue(idx);
                            staticCircle.setOccupied(true);
                        }

                        handleCurrentSituation(temp, staticCircle);
                        boolean result = game.isCurrentTaskSolved();
                        // backtrack
                        game.getCurrentTask().clearPlaces();

                        if (result && !paused) {
                            game.incrementCompletedTasks();
                            doPopUp();
                        }

                    } else {
                        // If there is no intersection
                        if (staticCircle.getValue() == cidx) {
                            temp.setValue(Circle.LEAVE);
                            staticCircle.setValue(Circle.LEAVE);
                            staticCircle.setOccupied(false);
                            Log.d("ED1", "Leaving static circle");
                        }
                    }
                }
            }

            if (maxVR != -1) {
                sumForceX += maxdX * 2.5;
                sumForceY += maxdY * 2.5;
            }

            /*
            if (occupiedReversedForce) {
                sumForceX = -sumForceX;
                sumForceY = -sumForceY;
            }
            */

            temp.updateDragForce(-sumForceX, -sumForceY, delta);

            // Clear max
            maxdX = 0;
            maxdY = 0;
            maxVR = -1;
        }

    }

    private void handleCurrentSituation(Circle temp, Circle staticCircle) {
        int gameNum = (game.getChosenGame() == 0) ? game.getChosenLevel() : game.getChosenGame();
        switch (gameNum) {
            case 1:
                game.getCurrentTask().setOperation(temp.getOperation());
                break;
            case 2:
                game.getCurrentTask().setEmptyAnimal(temp.getAnimal());
                break;
            case 3:
                for (Circle circle : staticCircles) {
                    int value = circle.getValue();
                    if (value == Circle.DEFAULT_CIRCLE_VALUE || value == Circle.LEAVE) {
                        continue;
                    }
                    int staticCircleID = circle.getSideIndex();
                    Animal animal = circles.get(value).getAnimal();
                    if (staticCircleID == Circle.LEFT_SIDE_INDEX) {
                        game.getCurrentTask().addAnimalToLeftPlace(animal);
                    } else if (staticCircleID == Circle.MIDDLE_INDEX) {
                        game.getCurrentTask().addAnimalToMiddlePlace(animal);
                    } else if (staticCircleID == Circle.RIGHT_SIDE_INDEX) {
                        game.getCurrentTask().addAnimalToRightPlace(animal);
                    }
                }
                break;
            case 4:
                HashSet<Animal> var_x = new HashSet<Animal>();
                HashSet<Animal> var_y = new HashSet<Animal>();
                for (Circle circle : staticCircles) {
                    Animal keyAnimal = circle.getAnimal();
                    if (keyAnimal == Animal.EMPTY) {
                        int value = circle.getValue();
                        if (value == Circle.DEFAULT_CIRCLE_VALUE || value == Circle.LEAVE) {
                            // static circle is not occupied
                            var_x.add(null);
                        } else {
                            Animal animal = circles.get(value).getAnimal();
                            var_x.add(animal);
                        }
                    } else if (keyAnimal == Animal.EMPTY2) {
                        int value = circle.getValue();
                        if (value == Circle.DEFAULT_CIRCLE_VALUE || value == Circle.LEAVE) {
                            // static circle is not occupied
                            var_y.add(null);
                        } else {
                            Animal animal = circles.get(value).getAnimal();
                            var_y.add(animal);
                        }
                    }
                }
                game.getCurrentTask().setVariable_x(var_x);
                game.getCurrentTask().setVariable_y(var_y);
                break;
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
        int key = (game.isCustomMode()) ? game.getChosenLevel() : game.getChosenGame();
        int level = (game.isCustomMode()) ? 3 : game.getChosenLevel();
        switch (key) {
            case 1:
                circles = getFirstGameBottomRow(level);
                break;
            case 2:
            case 4:
                circles = getSecondFourthGameBottomRow(level);
                break;
            case 3:
                circles = getThirdGameBottomRow();
        }
        return circles;
    }


    public ArrayList<Circle> getFirstGameBottomRow(int chosenLevel) {
        int y = canvas.Y_BOTTOM_ROW_CENTER;
        int x = canvas.X_BOTTOM_ROW_CENTER;
        int padding = 10;
        ArrayList<Circle> circles = new ArrayList<Circle>();
        final int radius = (int) Circle.radius_normal;
        int total_width;
        int startX;
        switch (chosenLevel) {
            case 1:
                total_width = 2 * (2 * radius + padding);
                startX = x - (total_width / 2) + (radius / 2);
                circles.add(new Circle(startX, y, false, context, null, Operation.EQUAL));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, false, context, null, Operation.NOT_EQUAL));
                break;
            case 2:
            case 3:
                total_width = 3 * (2 * radius + padding);
                startX = x - (total_width / 2) + (radius / 2);
                circles.add(new Circle(startX, y, false, context, null, Operation.EQUAL));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, false, context, null, Operation.GREATER_THAN));
                startX += (2 * radius) + padding;
                circles.add(new Circle(startX, y, false, context, null, Operation.LESS_THAN));
                break;

        }
        return circles;
    }

    public ArrayList<Circle> getSecondFourthGameBottomRow(int chosenLevel) {
        int y = canvas.Y_BOTTOM_ROW_CENTER;
        int x = canvas.X_BOTTOM_ROW_CENTER;
        int padding = 10;
        ArrayList<Circle> circles = new ArrayList<Circle>();
        final int radius = (int) Circle.radius_normal;
        int total_width;
        int startX;
        int numberOfAnimals = (chosenLevel == 3) ? Animal.hard.length : Animal.easy.length;
        int numberOfEveryAnimal = 1;
        if (game.getChosenGame() == 4 || (game.getChosenGame()==0 && game.getChosenLevel()==4)) {
            numberOfEveryAnimal = Generator.GAME_4_MAX_OF_VARIABLE_LEVEL_2_3;
        }

        total_width = numberOfAnimals * (2 * radius + padding);
        startX = x - (total_width / 2) + (radius / 2);

        // mouse
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.MOUSE, null));
        }
        startX += (2 * radius) + padding;
        // cat
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.CAT, null));
        }
        startX += (2 * radius) + padding;
        // goose
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.GOOSE, null));
        }
        startX += (2 * radius) + padding;
        // dog
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.DOG, null));
        }
        startX += (2 * radius) + padding;
        // goat
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.GOAT, null));
        }
        startX += (2 * radius) + padding;
        // ram
        for (int i = 0; i < numberOfEveryAnimal; i++) {
            circles.add(new Circle(startX, y, false, context, Animal.RAM, null));
        }
        startX += (2 * radius) + padding;
        switch (chosenLevel) {
            case 3:
                // cow
                for (int i = 0; i < numberOfEveryAnimal; i++) {
                    circles.add(new Circle(startX, y, false, context, Animal.COW, null));
                }
                startX += (2 * radius) + padding;
                // horse
                for (int i = 0; i < numberOfEveryAnimal; i++) {
                    circles.add(new Circle(startX, y, false, context, Animal.HORSE, null));
                }
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
            game.getCurrentTask().resize(canvas.canvasWidth, canvas.canvasHeight);
            circles = getBottomRowCircles();
        }

        // Unblock
        this.paused = false;
        clearDependencies();
    }

    public void resizeWidget(int w, int h) {
        popUpWidget.resize(w, h);
    }


    public ArrayList<Circle> getThirdGameBottomRow() {
        int y = canvas.Y_BOTTOM_ROW_CENTER;
        int x = canvas.X_BOTTOM_ROW_CENTER;
        int padding = 10;
        ArrayList<Circle> circles = new ArrayList<Circle>();
        final int radius = (int) Circle.radius_normal;
        int total_width;
        int startX;
        HashMap<Animal, Integer> animalMap = game.getCurrentTask().getAnimalMap();

        total_width = animalMap.size() * (2 * radius + padding);
        startX = x - (total_width / 2) + (radius / 2);

        Iterator it = animalMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            int numberOfConcreteAnimal = (int) pair.getValue();
            Animal animal = (Animal) pair.getKey();
            for (int i = 0; i < numberOfConcreteAnimal; i++) {
                circles.add(new Circle(startX, y, false, context, animal, null));
            }
            startX += (2 * radius) + padding;
        }
        return circles;
    }

    public void setMovingStatus(boolean state) {
        this.isMoving = state;
    }

    private void clearDependencies() {
        for (Circle cr : circles) {
            cr.setValue(Circle.LEAVE);
        }

        for (Circle scr : staticCircles) {
            scr.setValue(Circle.LEAVE);
            scr.setOccupied(false);
        }
    }

    private int getDXfromLastTouch(int fromX) {
        return this.lastTouchX - fromX;
    }

    private int getDYfromLastTouch(int fromY) {
        return this.lastTouchY - fromY;
    }
}
