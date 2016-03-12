package com.example.beebzb.bakalarka.entity;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Generator {
    private static final int MAX_OF_CIRCLES_IN_TASK = 10;

    public Task getTaskGame1Level12() {
        Task task = new Task();
        int leftSideNumber = 1 + (int) (Math.random() * ((MAX_OF_CIRCLES_IN_TASK - 1 - 1) + 1));
        int max_on_right_side = MAX_OF_CIRCLES_IN_TASK - leftSideNumber;
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

    public ArrayList<Task> generateTasksForFirstGame(int number_of_tasks, int chosenLevel) {
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
        int leftSideNumber = 1 + (int) (Math.random() * ((MAX_OF_CIRCLES_IN_TASK - 1 - 1) + 1));
        int max_on_right_side = MAX_OF_CIRCLES_IN_TASK - leftSideNumber;
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
}
