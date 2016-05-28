package com.example.beebzb.bakalarka.entity;

import android.content.Context;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.beebzb.bakalarka.ChooseLevelActivity;
import com.example.beebzb.bakalarka.EditorActivity;
import com.example.beebzb.bakalarka.GameActivity;
import com.example.beebzb.bakalarka.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Game {
    private int chosenGame;
    public static int chosenLevel;
    private Task currentTask;
    private Solver solver;
    private Generator generator;
    private ArrayList<Task> tasks = new ArrayList<Task>();
    public static final int NUMBER_OF_TASKS = 5;
    public static final int NUMBER_OF_CUSTOM_TASK = 1;
    private int completed_tasks = 0;
    private int color;
    private int numberOfTasks = 0;
    private boolean customMode = false;
    private Context context;

    public Game(Context context, int color, int chosenGame, int chosenLevel) {
        this.color = color;
        this.context = context;
        this.chosenGame = chosenGame;
        this.chosenLevel = chosenLevel;
        customMode = chosenGame == 0;
        if (customMode){
            tasks.add(getCustomTaskFromSP(chosenLevel));
            numberOfTasks = NUMBER_OF_CUSTOM_TASK;
            solver = new Solver(chosenLevel);
        }
        else {
            numberOfTasks = NUMBER_OF_TASKS;
            generator = new Generator(chosenGame, chosenLevel);
            tasks = generator.generateTasks(numberOfTasks);
            solver = new Solver(chosenGame);


        }
        getNextTask();
        //Test.main();
    }

    private Task getCustomTaskFromSP(int chosenLevel) {
        Gson gson = new Gson();
        String key = ChooseLevelActivity.getKeyByChosenLevel(chosenLevel);
        String json = PreferenceManager.getDefaultSharedPreferences(context).getString(key, EditorActivity.DEFAULT_TASK);
        Task task = gson.fromJson(json, Task.class);
        return task;
    }


    public Task getCurrentTask() {
        return currentTask;
    }

    public int getNUMBER_OF_TASKS() {
        return numberOfTasks;
    }

    public double getProgress() {
        return 100 / numberOfTasks * completed_tasks;
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
        return solver.isSolved(currentTask);
    }

    public boolean areAllTasksCompleted() {
        return completed_tasks == numberOfTasks;
    }

    public void incrementCompletedTasks() {
        completed_tasks++;
    }

    public int getColor() {
        return color;
    }

    public boolean isCustomMode() {
        return customMode;
    }
}
