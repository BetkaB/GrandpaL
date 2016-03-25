package com.example.beebzb.bakalarka.entity;

import android.graphics.Canvas;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Task {
    private ArrayList<Animal> leftSide = new ArrayList<Animal>();
    private ArrayList<Animal> rightSide = new ArrayList<Animal>();
    private Operation operation;

    private ArrayList<Circle> circles;
    private ArrayList<Circle> staticCicles = new ArrayList<Circle>();

    // useful only for second game
    private int emptyAnimalIndex = -1;
    private boolean emptyAnimalOnLeftSide = false;

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

    public void draw(Canvas canvas) {
        if (circles == null) {
            createCircles(canvas);
        }
        for (Circle circle : circles) {
            circle.draw(canvas);
        }
    }

    private void createCircles(Canvas canvas) {
        int y = GameHandler.canvas.getCanvasHeight() / 2;
        int x = GameHandler.canvas.X_BOTTOM_ROW_CENTER;
        circles = new ArrayList<Circle>();
        int padding = 10;
        int totalCircles = leftSide.size() + rightSide.size() + 1;
        final int radius = 60;
        int total_width = totalCircles * (2*radius + padding);
        int startX = x - (total_width / 2) + (radius / 2);
        for (Animal animal : leftSide) {
            if (animal == Animal.EMPTY){
                staticCicles.add(new Circle(startX, y, 70, true, GameHandler.context, animal, null));
                startX += (2 * radius) + padding;
            }
            else {
                circles.add(new Circle(startX, y, radius, true, GameHandler.context, animal, null));
                startX += (2 * radius) + padding;
            }
        }
        if (getOperation() == Operation.EMPTY) {
            staticCicles.add(new Circle(startX, y, 70, true, GameHandler.context, null, getOperation()));
        }
        else {
            circles.add(new Circle(startX, y, radius, true, GameHandler.context, null, getOperation()));
        }
        startX += (2 * radius) + padding;

        for (Animal animal : rightSide) {
            if (animal == Animal.EMPTY){
                staticCicles.add(new Circle(startX, y, 70, true, GameHandler.context, animal, null));
                startX += (2 * radius) + padding;
            }
            else {
                circles.add(new Circle(startX, y, radius, true, GameHandler.context, animal, null));
                startX += (2 * radius) + padding;
            }
        }
    }


    public int getEmptyAnimalIndex() {
        return emptyAnimalIndex;
    }

    public void setEmptyAnimalIndex(int emptyAnimalIndex) {
        this.emptyAnimalIndex = emptyAnimalIndex;
    }

    public ArrayList<Circle> getStaticCircles(){
        return  staticCicles;
    }

    @Override
    public String toString() {
        return "Task{" +
                ", leftSide=" + leftSide +
                "operation=" + operation +
                ", rightSide=" + rightSide +
                '}';
    }


    public void setEmptyAnimal(Animal newAnimal){
        if (emptyAnimalOnLeftSide){
            leftSide.set(0,newAnimal);
        }
        else {
            rightSide.set(0,newAnimal);
        }
       /* for (int i = 0 ; i < leftSide.size()-1 ; i++){
            if (leftSide.get(i) == Animal.EMPTY){
                leftSide.set(i, newAnimal);
                //indexOfCurrentlyGuessedAnimal = i;
                return true;
            }

        }
        for (int i = 0 ; i < rightSide.size()-1 ; i++){
            if (rightSide.get(i) == Animal.EMPTY){
                rightSide.set(i, newAnimal);
               // indexOfCurrentlyGuessedAnimal = i;
                return true;
            }

        }*/

    }
}
