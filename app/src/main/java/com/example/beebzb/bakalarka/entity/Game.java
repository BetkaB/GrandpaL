package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import java.util.ArrayList;

public class Game {
    private int chosenGame;
    private int chosenLevel;
    private Task currentTask;
    private Solver solver = new Solver();
    private Generator generator;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    private final int NUMBER_OF_TASKS = 5;
    private int completed_tasks = 0;
    private int color;

    public Game(int color, int chosenGame, int chosenLevel) {
        this.color = color;
        generator = new Generator(chosenGame, chosenLevel);
        this.chosenGame = chosenGame;
        this.chosenLevel = chosenLevel;
        tasks = generator.generateTasks(NUMBER_OF_TASKS);
        getNextTask();
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public int getNUMBER_OF_TASKS() {
        return NUMBER_OF_TASKS;
    }

    public double getProgress() {
        return 100 / NUMBER_OF_TASKS * completed_tasks;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }

    public void getNextTask() {
        setCurrentTask(tasks.get(completed_tasks));
    }

    public int getChosenLevel() {
        return chosenLevel;
    }

    public int getChosenGame() {
        return chosenGame;
    }

    public boolean isCurrentTaskSolved() {
        Log.d("BUG_SECOND_GAME", currentTask.toString());
        return solver.isSolved(currentTask);
    }

    public boolean areAllTasksCompleted(){
        return completed_tasks == NUMBER_OF_TASKS;
    }

    public void incrementCompletedTasks(){
        completed_tasks++;
    }

    public int getColor() {
        return color;
    }

}
