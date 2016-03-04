package com.example.beebzb.bakalarka;


public class Game {
    private int color;
    private final int NUMBER_OF_TASKS = 5;
    private int tasks_completed = 4;

    public int getColor() {
        return color;
    }

    public Game(int color) {

        this.color = color;
    }

    public int getNUMBER_OF_TASKS(){
        return  NUMBER_OF_TASKS;
    }

    public double getProgress(){
        return 100 / NUMBER_OF_TASKS * tasks_completed;
    }
}
