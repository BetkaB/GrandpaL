package com.example.beebzb.bakalarka.entity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;


public class Circle {
    private Paint pnt;
    private double dx, dy, dragX, dragY;
    public static final int DEFAULT_CIRCLE_VALUE = -1;
    private int value = DEFAULT_CIRCLE_VALUE;
    public float x;
    public float y;
    public float radius = 60;
    public static final float radius_empty = 50;
    public static final float radius_normal = 40;
    public boolean isStatic = false;
    private boolean isOccupied = false;
    private Animal animal;
    private Operation operation = null;

    private int sideIndex = -1;
    public static final int LEFT_SIDE_INDEX = 0;
    public static final int RIGHT_SIDE_INDEX = 1;
    public static final int MIDDLE_INDEX = 2;

    private Context context;

    public static final int LEAVE = -100;

    public Circle(int x, int y, boolean staticState, Context context, Animal animal, Operation operation) {
        this.context = context;
        this.x = x;
        this.animal = animal;
        this.operation = operation;
        this.y = y;
        this.isStatic = staticState;
        this.dragX = 0;
        this.dragY = 0;
        this.radius = radius_normal;
        this.pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (animal == Animal.EMPTY || animal == Animal.EMPTY2 || operation == Operation.EMPTY){
            this.radius = radius_empty;
        }
    }

    public Circle(int x, int y, int radius, Context context, Animal animal) {
        this.context = context;
        this.x = x;
        this.animal = animal;
        this.y = y;
        this.isStatic = true;
        this.dragX = 0;
        this.dragY = 0;
        this.radius = radius;
        this.pnt = new Paint(Paint.ANTI_ALIAS_FLAG);
    }



    public void draw(Canvas canvas) {
        if (operation != null) {
            Drawable d = context.getResources().getDrawable(operation.getDrawableInt());
            d.setBounds((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius));
            d.draw(canvas);
        }
        else {
            Drawable d = context.getResources().getDrawable(animal.getDrawableInt());
            d.setBounds((int) (x - radius), (int) (y - radius), (int) (x + radius), (int) (y + radius));
            d.draw(canvas);
        }
    }

    public void moveDrag(float newX, float newY) {
        this.dx = this.x - newX;
        this.dy = this.y - newY;
    }

    public void updateMove() {
        this.x = Math.round(this.x - this.dragX - this.dx / 2);
        this.y = Math.round(this.y - this.dragY - this.dy / 2);
    }



    public void updateDragForce(int dgx, int dgy, double dt) {
        this.dragX = dgx;
        this.dragY = dgy;

        updateMove();
    }

    public void clearDXY() {
        this.dx = 0;
        this.dy = 0;
    }

    public void setColor(String color) {
        this.pnt.setColor(Color.parseColor(color));
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void setOperation(Operation operation){
        if (isStatic ){
            this.operation = operation;
        }
    }

    public void setAnimal(Animal animal){
        if (isStatic){
            this.animal = animal;
        }
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOccupied(boolean state) {
        this.isOccupied = state;
    }

    public boolean isOccupied() {return this.isOccupied;}

    public Animal getAnimal() {
        return animal;
    }

    public int getSideIndex() {
        return sideIndex;
    }

    public void setSideIndex(int index){
        if (isStatic) {
            sideIndex = index;
        }
    }

    public float getRadius() {
        return radius;
    }
}

