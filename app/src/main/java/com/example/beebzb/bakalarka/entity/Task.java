package com.example.beebzb.bakalarka.entity;

import android.graphics.Canvas;

import com.example.beebzb.bakalarka.MyCanvas;
import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;
import java.util.HashMap;

public class Task {
    private ArrayList<Animal> leftSide = new ArrayList<Animal>();
    private ArrayList<Animal> rightSide = new ArrayList<Animal>();
    private Operation operation;
    private final int chosenGame;

    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCircles = new ArrayList<Circle>();

    // useful only for second game
    private int emptyAnimalIndex = -1;
    private boolean emptyAnimalOnLeftSide = false;

    // useful for third game
    private Fence fence;
    //Circle[][] staticCirclesThirdGame = new Circle[][];
    private HashMap<Animal, Integer> animalMap = null;

    // useful for fourth game Boolean onLeftSide
    private HashMap <Animal, Boolean> variable_x;
    private HashMap <Animal, Boolean> variable_y;

    public Task(int chosenGame, int level) {
        this.chosenGame = chosenGame;

        if (this.chosenGame == 3)
            this.fence = new Fence(level);
    }

    public HashMap<Animal, Integer> getAnimalMap() {
        return animalMap;
    }

    public void setAnimalMap(HashMap<Animal, Integer> animalMap) {
        this.animalMap = animalMap;

    }

    public void setEmptyAnimalOnLeftSide(boolean emptyAnimalOnLeftSide) {
        this.emptyAnimalOnLeftSide = emptyAnimalOnLeftSide;
    }

    public boolean isEmptyAnimalOnLeftSide() {
        return emptyAnimalOnLeftSide;
    }

    public void addToLeftSide(Animal animal) {
        leftSide.add(animal);
    }

    public void addToRightSide(Animal animal) {
        rightSide.add(animal);
    }

    public ArrayList<Animal> getLeftSide() {
        return leftSide;
    }

    public ArrayList<Animal> getRightSide() {
        return rightSide;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public void setRightSide(ArrayList<Animal> rightSide) {
        this.rightSide = rightSide;
    }

    public void setLeftSide(ArrayList<Animal> leftSide) {
        this.leftSide = leftSide;
    }

    public void draw(Canvas canvas) {

        if (chosenGame == 3) {
            this.fence.draw(canvas);

        } else {

            if (circles == null) {
                createCircles(canvas);
            }
            for (Circle circle : circles) {
                circle.draw(canvas);
            }
        }

    }

    private void createCircles(Canvas canvas) {
        int y = GameHandler.canvas.getCanvasHeight() / 2;
        int x = GameHandler.canvas.X_BOTTOM_ROW_CENTER;
        circles = new ArrayList<Circle>();
        int padding = 14;
        int totalCircles = leftSide.size() + rightSide.size() + 1;
        final int radius = (int) Circle.radius_normal;
        int total_width = totalCircles * (2 * radius + padding);
        int startX = x - (total_width / 2) + (radius / 2);
        for (Animal animal : leftSide) {
            if (animal == Animal.EMPTY || animal == Animal.EMPTY2) {
                staticCircles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
            }
            else {
                circles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
            }
            startX += (2 * Circle.radius_normal) + padding;

        }
        if (getOperation() == Operation.EMPTY) {
            staticCircles.add(new Circle(startX, y, true, GameHandler.context, null, getOperation()));
        } else {
            circles.add(new Circle(startX, y, true, GameHandler.context, null, getOperation()));

        }
        startX += (2 * Circle.radius_normal) + padding;

        for (Animal animal : rightSide) {
            if (animal == Animal.EMPTY || animal == Animal.EMPTY2) {
                staticCircles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
            } else {
                circles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
            }
            startX += (2 * Circle.radius_normal) + padding;
        }

    }


    public int getEmptyAnimalIndex() {
        return emptyAnimalIndex;
    }

    public void setEmptyAnimalIndex(int emptyAnimalIndex) {
        this.emptyAnimalIndex = emptyAnimalIndex;
    }

    public ArrayList<Circle> getStaticCircles() {
        return staticCircles;
    }

    @Override
    public String toString() {
        if (chosenGame == 3) {
            return "Task "  + animalMap.toString();
        }
        return "Task{" +
                ", leftSide=" + leftSide +
                "operation=" + operation +
                ", rightSide=" + rightSide +
                '}';
    }


    public void setEmptyAnimal(Animal newAnimal) {
        if (emptyAnimalOnLeftSide) {
            leftSide.set(0, newAnimal);
        } else {
            rightSide.set(0, newAnimal);
        }

    }

    public void resize(int canvasW, int canvasH) {
        if (chosenGame == 3) {
            this.fence.resize(canvasW, canvasH - MyCanvas.PROGRESS_BAR_BOTTOM_PADDING - MyCanvas.PROGRESS_BAR_HEIGHT - MyCanvas.BOTTOM_ROW_HEIGHT);
            createStaticCirclesFor3Game();
        }
    }

    private void createStaticCirclesFor3Game() {
        final int NUMBER_OF_STATIC_CIRCLES_INSIDE_ONE_FENCE = 9;

        // create INVISIBLE static circles in fences
        int oneFenceHeight = fence.getBottom() - fence.getTop();
        int oneFenceWidth = fence.getPartWidth();
        int radius;
        if (oneFenceHeight < fence.getPartWidth()) {
            radius = oneFenceHeight / 3 / 2;
        } else {
            radius = fence.getPartWidth() / 3 / 2;
        }

        int sidePadding = (oneFenceWidth - (6 * radius)) / 2;
        int bottomTopPadding = (oneFenceHeight - (6 * radius)) / 2;

        int x = fence.getLeft() + radius;
        int y = fence.getTop() + bottomTopPadding + radius;

        for (int i = 0; i < fence.getParts(); i++) {
            x = fence.getLeft() + (oneFenceWidth * i) + sidePadding + radius;
            y = fence.getTop() + bottomTopPadding + radius;
            for (int j = 0; j < NUMBER_OF_STATIC_CIRCLES_INSIDE_ONE_FENCE; j++) {
                staticCircles.add(new Circle(x, y, true, GameHandler.context, Animal.EMPTY, null));
                x += +(2 * radius);
                if (j % 3 == 2) {
                    y += +2 * radius;
                    x = fence.getLeft() + (oneFenceWidth * i) + sidePadding + radius;
                }
            }
        }
    }

}

