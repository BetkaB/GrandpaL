package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Generator {
    private final int MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL = 6;
    public static final int MAX_OF_CIRCLES_IN_THIRD_LEVEL = 10;
    public static final int GAME_3_MAX_OF_CIRCLES_LEVEL_3 = 18;
    public static final int GAME_3_MAX_OF_CIRCLES_LEVEL_1_2 = 12;

    // fourth game constants
    public static final int GAME_4_MAX_OF_VARIABLE_LEVEL_2_3 = 2;
    public static final int GAME_4__MIN_OF_VARIABLE_LEVEL_2_3 = 1;

    public static final int GAME_4_MAX_OF_VARIABLE_LEVEL_1 = 2;
    public static final int GAME_4_MIN_OF_VARIABLE_LEVEL_1 = 2;



    private int chosenGame = -1;
    private int chosenLevel = -1;

    public Generator(int chosenGame, int chosenLevel) {
        this.chosenGame = chosenGame;
        this.chosenLevel = chosenLevel;
    }

    public ArrayList<Task> generateTasks(int number_of_tasks) {
        switch (chosenGame) {
            case 1:
                return generateTasksGame_1(number_of_tasks);
            case 2:
                return generateTasksGame_2(number_of_tasks);
            case 3:
                return generateTasksGame_3(number_of_tasks);
            default:
                return generateTasksGame_4(number_of_tasks);

        }


    }

    private ArrayList<Task> generateTasksGame_4(int number_of_tasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Solver solver = new Solver(4);
        while (number_of_tasks != 0){
            Task task = generateTaskGame_4(chosenLevel);
            int solutions = solver.getNumberOfSolutionGame_4(task, chosenLevel==3);
            if (solutions > 0 && solutions <= 5){
                tasks.add(task);
                number_of_tasks--;
            }
        }

        return tasks;
    }

    private Task generateTaskGame_4(int chosenLevel) {
        Random random =  new Random();
        Task task = new Task(chosenGame, chosenLevel);
        task.setOperation(Operation.EQUAL);
        int max_of_circles = (chosenLevel == 3) ? MAX_OF_CIRCLES_IN_THIRD_LEVEL : MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL;
        int numberOfVariableX = getNumberOfVariableX();
        int numberOfVariableY = getNumberOfVariableY();
        int min_of_circles = numberOfVariableX + numberOfVariableY;
        int numberOfCircles = random.nextInt((max_of_circles - min_of_circles) + 1) + min_of_circles;
        int leftSide = random.nextInt(numberOfCircles);
        int rightSide = numberOfCircles - leftSide;

        for (int x = 0; x < numberOfVariableX; x++){
            if (random.nextBoolean()){
                task.addToLeftSide(Animal.EMPTY);
                leftSide--;
            }
            else {
                task.addToRightSide(Animal.EMPTY);
                rightSide--;
            }

        }

        for (int y = 0; y < numberOfVariableY; y++){
            if (random.nextBoolean()){
                task.addToLeftSide(Animal.EMPTY2);
                leftSide--;
            }
            else {
                task.addToRightSide(Animal.EMPTY2);
                rightSide--;
            }

        }

        for (int l = 0; l < leftSide; l++){
            task.addToLeftSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }
        for (int r = 0; r < rightSide; r++){
            task.addToRightSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }

        Log.e("GENERATOR",task.toString());
        return task;
    }

    private int getNumberOfVariableX() {
        Random random = new Random();
        int min = GAME_4_MIN_OF_VARIABLE_LEVEL_1;
        int max = GAME_4_MAX_OF_VARIABLE_LEVEL_1;
        if (chosenLevel != 1) {
            min = GAME_4__MIN_OF_VARIABLE_LEVEL_2_3;
            max = GAME_4_MAX_OF_VARIABLE_LEVEL_2_3;
        }
        return random.nextInt((max - min) + 1) + min;

    }

    private int getNumberOfVariableY() {
        if (chosenLevel == 1) {
            return 0;
        }
        Random random = new Random();
        int min = GAME_4__MIN_OF_VARIABLE_LEVEL_2_3;
        int max = GAME_4_MAX_OF_VARIABLE_LEVEL_2_3;
        return random.nextInt((max - min) + 1) + min;
    }

    private ArrayList<Task> generateTasksGame_3(int number_of_tasks) {
        Random random = new Random();
        int maxOfAnimals = (chosenLevel == 1) ? GAME_3_MAX_OF_CIRCLES_LEVEL_1_2 : GAME_3_MAX_OF_CIRCLES_LEVEL_3;
        int minOfAnimals = 3;

        ArrayList<Task> tasks = new ArrayList<Task>();

        for (int t = 0; t < number_of_tasks; t++) {
            HashMap<Animal, Integer> animalMap = new HashMap<>();
            Task task = new Task(chosenGame, chosenLevel);
            int numberOfAnimal = random.nextInt((maxOfAnimals - minOfAnimals) + 1) + minOfAnimals;
            for (int a = 0; a < numberOfAnimal; a++) {
                Animal animal = Animal.getRandomAnimalBasedOnLevel(chosenLevel);
                if (animalMap.containsKey(animal)) {
                    animalMap.put(animal, animalMap.get(animal) + 1);
                } else {
                    animalMap.put(animal, 1);
                }
            }
            Log.e("DEBUG_3_GAME", "animal map  " + animalMap.toString());
            task.setAnimalMap(animalMap);
            tasks.add(task);
        }
        return tasks;
    }

    public ArrayList<Task> generateTasksGame_1(int number_of_tasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        int i = 0;
        while (i != number_of_tasks) {
            tasks.add(generateTaskGame_1());
            i++;
        }
        return tasks;
    }

    public Task generateTaskGame_1() {
        Task task = new Task(chosenGame, chosenLevel);
        int max_on_one_side = MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL / 2;
        if (chosenLevel == 3) {
            max_on_one_side = MAX_OF_CIRCLES_IN_THIRD_LEVEL / 2;
        }

        int sizes[] = getRandomSize(max_on_one_side);
        int leftSideNumber = sizes[0];
        int rightSideNumber = sizes[1];

        for (int i = 0; i < leftSideNumber; i++) {
            task.addToLeftSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }
        for (int i = 0; i < rightSideNumber; i++) {
            task.addToRightSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }
        task.setOperation(Operation.EMPTY);
        return task;
    }


    public ArrayList<Task> generateTasksGame_2(int number_of_tasks) {
        ArrayList<Task> tasks = new ArrayList<Task>();
        int totalCount = 0;
        Solver solver = new Solver(chosenGame);
        boolean useAllAnimals = chosenLevel == 3;
        while (totalCount != number_of_tasks) {
            Task task = generateTaskGame_2(chosenLevel);
            int solutions = solver.getNumberOfSolutionGame_1_2(task, useAllAnimals);
            if (solutions >= 1) {
                Log.e("GENERATOR", "2 game with " + solutions + " solutions " + task.toString());
                tasks.add(task);
                totalCount++;
            }
        }
        return tasks;
    }

    private Task generateTaskGame_2(int chosenLevel) {
        Operation operation = null;
        Random random = new Random();
        int randEmpty = random.nextInt(2);
        int max_on_one_side = MAX_OF_CIRCLES_IN_FIRST_SECOND_LEVEL / 2;

        switch (chosenLevel) {
            case 1:
                int randOpe = random.nextInt(2);
                if (randOpe == 1) {
                    operation = Operation.GREATER_THAN;
                } else {
                    operation = Operation.LESS_THAN;
                }
                break;
            case 2:
                operation = Operation.EQUAL;
                break;
            case 3:
                operation = Operation.EQUAL;
                max_on_one_side = MAX_OF_CIRCLES_IN_THIRD_LEVEL / 2;
                break;
        }
        Task task = new Task(chosenGame, chosenLevel);
        task.setOperation(operation);

        int sizes[] = getRandomSize(max_on_one_side);
        int leftSideNumber = sizes[0];
        int rightSideNumber = sizes[1];

        if (randEmpty == 1) {
            task.addToLeftSide(Animal.EMPTY);
            task.setEmptyAnimalOnLeftSide(true);
            leftSideNumber--;
        } else {
            task.addToRightSide(Animal.EMPTY);
            rightSideNumber--;
        }
        for (int i = 0; i < leftSideNumber; i++) {
            task.addToLeftSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }
        for (int i = 0; i < rightSideNumber; i++) {
            task.addToRightSide(Animal.getRandomAnimalBasedOnLevel(chosenLevel));
        }

        return task;
    }

    private int[] getRandomSize(int max_on_one_side) {
        int leftSideNumber = 1 + (int) (Math.random() * ((max_on_one_side - 1) + 1)); // 1 - 3 // 1 - 5

        int[] temp;
        if (leftSideNumber == max_on_one_side) {
            temp = new int[]{-1, 0};
        } else if (leftSideNumber == 1) {
            temp = new int[]{0, 1};
        } else {
            temp = new int[]{-1, 0, 1};
        }
        Random random = new Random();
        int randomIndex = random.nextInt(temp.length);

        int rightSideNumber = leftSideNumber + temp[randomIndex];
        return new int[]{leftSideNumber, rightSideNumber};
    }


}
