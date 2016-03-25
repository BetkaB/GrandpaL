package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;
import java.util.Random;

public class Generator {
    private final int MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL = 6;
    private final int MAX_OF_CIRCLES_IN_THIRD_LEVEL = 10;

    private int chosenGame = -1;
    private int chosenLevel = -1;

    public Generator(int chosenGame, int chosenLevel) {
        this.chosenGame = chosenGame;
        this.chosenLevel = chosenLevel;
    }

    public Task getTaskGame1Level12() {
        Task task = new Task();
        int leftSideNumber = 1 + (int) (Math.random() * ((MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL - 1 - 1) + 1));
        int max_on_right_side = MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL - leftSideNumber;
        int rightSideNumber = 1 + (int) (Math.random() * ((max_on_right_side - 1) + 1));
        for (int i = 0; i < leftSideNumber; i++) {
            task.addToLeftSide(Animal.randomAnimal());
        }
        for (int i = 0; i < rightSideNumber; i++) {
            task.addToRightSide(Animal.randomAnimal());
        }
        task.setOperation(Operation.EMPTY);
        return task;
    }

    public ArrayList<Task> generateTasksForFirstGame(int number_of_tasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        switch (chosenLevel) {
            case 1:
            case 2:
                for (int i = 0; i < number_of_tasks; i++) {
                    tasks.add(getTaskGame1Level12());
                }
                break;
            case 3:
                for (int i = 0; i < number_of_tasks; i++) {
                    tasks.add(getTaskGame1Level3());
                }
                break;
        }
        return tasks;
    }

    public Task getTaskGame1Level3() {
        Task task = new Task();
        int leftSideNumber = 1 + (int) (Math.random() * ((MAX_OF_CIRCLES_IN_THIRD_LEVEL - 1 - 1) + 1));
        int max_on_right_side = MAX_OF_CIRCLES_IN_THIRD_LEVEL - leftSideNumber;
        int rightSideNumber = 1 + (int) (Math.random() * ((max_on_right_side - 1) + 1));
        for (int i = 0; i < leftSideNumber; i++) {
            task.addToLeftSide(Animal.randomFromAllAnimals());
        }
        for (int i = 0; i < rightSideNumber; i++) {
            task.addToRightSide(Animal.randomFromAllAnimals());
        }
        task.setOperation(Operation.EMPTY);
        return task;
    }

    public ArrayList<Task> generateTasksForSecondGame(int number_of_tasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        int totalCount = 0;
        Solver solver =  new Solver();
        boolean useAllAnimals = chosenLevel == 3;
        while (totalCount != number_of_tasks){
            Task task = generateTaskGame2(chosenLevel);
            int solutions = solver.getNumberOfSolution(task,useAllAnimals);
            if (solutions >= 1 ){
                Log.e("GENERATOR", "2 game with " +solutions+" solutions "+ task.toString());
                tasks.add(task);
                totalCount++;
            }

        }
        return  tasks;


    }

    private Task generateTaskGame2(int chosenLevel){
        Operation operation = null;
        Random random = new Random();
        boolean emptyAnimalOnLeftSide = false;
        int randEmpty = random.nextInt(2);
        int max = 0;

        switch (chosenLevel){
            case 1:
                int randOpe = random.nextInt(2);
                if (randOpe == 1){
                    operation = Operation.GREATER_THAN;
                }
                else {
                    operation =  Operation.LESS_THAN;
                }
                max = MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL;
                break;
            case 2:
                operation = Operation.EQUAL;
                max = MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL;
                break;
            case 3:
                operation = Operation.EQUAL;
                max = MAX_OF_CIRCLES_IN_THIRD_LEVEL;

                break;

        }
        Task task = new Task();
        task.setOperation(operation);

        int leftSideNumber = 1 + (int) (Math.random() * ((max - 1 - 1) + 1));
        int max_on_right_side = max - leftSideNumber;
        int rightSideNumber = 1 + (int) (Math.random() * ((max_on_right_side - 1) + 1));


        if (randEmpty == 1){
            task.addToLeftSide(Animal.EMPTY);
            task.setEmptyAnimalOnLeftSide(true);
            leftSideNumber--;
        }
        else {
            task.addToRightSide(Animal.EMPTY);
            rightSideNumber--;
        }
        for (int i = 0; i < leftSideNumber; i++) {
            task.addToLeftSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }
        for (int i = 0; i < rightSideNumber; i++) {
            task.addToRightSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }



        return  task;
    }


    public ArrayList<Task> generateTasks(int number_of_tasks) {
        switch (chosenGame){
            case 1:
                return generateTasksForFirstGame(number_of_tasks);
            default:
                Log.e("GENERATOR", "generating for second game");
                return generateTasksForSecondGame(number_of_tasks);
        }

    }
}
