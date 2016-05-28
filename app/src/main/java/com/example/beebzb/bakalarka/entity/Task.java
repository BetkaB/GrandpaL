package com.example.beebzb.bakalarka.entity;

import android.graphics.Canvas;

import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Task {
    private ArrayList<Animal> leftSide = new ArrayList<Animal>();
    private ArrayList<Animal> rightSide = new ArrayList<Animal>();
    private Operation operation;
    private final int chosenGame;
    private final int chosenLevel;

    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCircles = new ArrayList<Circle>();

    // useful only for second game
    private int emptyAnimalIndex = -1;
    private boolean emptyAnimalOnLeftSide = false;

    // useful for third game
    private Fence fence;
    private ArrayList<Animal> leftPlace = new ArrayList<>();
    private ArrayList<Animal> middlePlace = new ArrayList<>();
    private ArrayList<Animal> rightPlace = new ArrayList<>();
    private HashMap<Animal, Integer> animalMap = null;

    // useful for fourth game Boolean onLeftSide
    private HashSet<Animal> variable_y;
    private HashSet<Animal> variable_x;

    public int getChosenLevel() {
        return chosenLevel;
    }

    public Task(int chosenGame, int level) {
        this.chosenGame = chosenGame;
        this.chosenLevel = level;

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
        Circle tempCircle;
        for (Animal animal : leftSide) {
            if (animal == Animal.EMPTY || animal == Animal.EMPTY2) {
                tempCircle = new Circle(startX, y, true, GameHandler.context, animal, null);
                tempCircle.setSideIndex(Circle.LEFT_SIDE_INDEX);
                staticCircles.add(tempCircle);
                startX += (2 * Circle.radius_empty) + padding;

            } else {
                circles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
                startX += (2 * Circle.radius_normal) + padding;
            }


        }
        if (getOperation() == Operation.EMPTY) {
            staticCircles.add(new Circle(startX, y, true, GameHandler.context, null, getOperation()));
        } else {
            circles.add(new Circle(startX, y, true, GameHandler.context, null, getOperation()));

        }
        startX += (2 * Circle.radius_normal) + padding;

        for (Animal animal : rightSide) {
            if (animal == Animal.EMPTY || animal == Animal.EMPTY2) {
                tempCircle = new Circle(startX, y, true, GameHandler.context, animal, null);
                tempCircle.setSideIndex(Circle.RIGHT_SIDE_INDEX);
                staticCircles.add(tempCircle);
                startX += (2 * Circle.radius_empty) + padding;

            } else {
                circles.add(new Circle(startX, y, true, GameHandler.context, animal, null));
                startX += (2 * Circle.radius_normal) + padding;
            }

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
            return "Task " + animalMap.toString();
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
        Circle tempCircle;
        int index = -1;

        for (int i = 0; i < fence.getParts(); i++) {
            x = fence.getLeft() + (oneFenceWidth * i) + sidePadding + radius;
            y = fence.getTop() + bottomTopPadding + radius;
            if (i == 0) {
                index = Circle.LEFT_SIDE_INDEX;
            }
            if (i == 1) {
                index = (fence.getParts() == 2) ? Circle.RIGHT_SIDE_INDEX : Circle.MIDDLE_INDEX;
            }
            if (i == 2) {
                index = Circle.RIGHT_SIDE_INDEX;
            }
            for (int j = 0; j < NUMBER_OF_STATIC_CIRCLES_INSIDE_ONE_FENCE; j++) {
                tempCircle = new Circle(x, y, (int) Circle.radius_empty, GameHandler.context, Animal.EMPTY_INVISIBLE);
                tempCircle.setSideIndex(index);
                staticCircles.add(tempCircle);
                x += +(2 * radius);
                if (j % 3 == 2) {
                    y += +2 * radius;
                    x = fence.getLeft() + (oneFenceWidth * i) + sidePadding + radius;
                }
            }
        }
    }

    public Fence getFence() {
        return fence;
    }

    public void clearPlaces() {
        rightPlace = new ArrayList<>();
        middlePlace = new ArrayList<>();
        leftPlace = new ArrayList<>();
    }

    public void addAnimalToRightPlace(Animal animal) {
        rightPlace.add(animal);
    }

    public void addAnimalToMiddlePlace(Animal animal) {
        middlePlace.add(animal);
    }

    public void addAnimalToLeftPlace(Animal animal) {
        leftPlace.add(animal);
    }

    public ArrayList<Animal> getLeftPlace() {
        return leftPlace;
    }

    public ArrayList<Animal> getMiddlePlace() {
        return middlePlace;
    }

    public ArrayList<Animal> getRightPlace() {
        return rightPlace;
    }

    public HashSet<Animal> getVariable_y() {
        return variable_y;
    }

    public void setVariable_y(HashSet<Animal> variable_y) {
        this.variable_y = variable_y;
    }

    public HashSet<Animal> getVariable_x() {
        return variable_x;
    }

    public void setVariable_x(HashSet<Animal> variable_x) {
        this.variable_x = variable_x;
    }


}

