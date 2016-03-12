package com.example.beebzb.bakalarka.entity;

import android.graphics.Canvas;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Task {
    private ArrayList<Animal> leftSide = new ArrayList<Animal>();
    private ArrayList<Animal> rightSide = new ArrayList<Animal>();
    private ArrayList<Circle> staticCicles = new ArrayList<Circle>();
    private Operation operation;
    private boolean completed = false;

    private ArrayList<Circle> circles;

    public void addToLeftSide(Animal animal) {
        leftSide.add(animal);
    }

    public void addToRightSide(Animal animal) {
        rightSide.add(animal);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
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
        int y = Game.canvas.getCanvasHeight() / 2;
        int x = Game.canvas.X_BOTTOM_ROW_CENTER;
        circles = new ArrayList<Circle>();
        int padding = 10;
        int totalCircles = leftSide.size() + rightSide.size() + 1;
        final int radius = 60;
        int total_width = totalCircles * (2*radius + padding);
        int startX = x - (total_width / 2) + (radius / 2);
        for (Animal animal : leftSide) {
            circles.add(new Circle(startX, y, radius, true, Game.context, animal, null ));
            startX += (2 * radius) + padding;
        }
        staticCicles.add(new Circle(startX, y, 70, true, Game.context, null, getOperation()));

        startX += (2 * radius) + padding;
        for (Animal animal : rightSide) {
            circles.add(new Circle(startX, y, radius, true, Game.context, animal, null));
            startX += (2 * radius) + padding;
        }
    }

    public ArrayList<Circle> getStaticCircles(){
        return  staticCicles;
    }

}
