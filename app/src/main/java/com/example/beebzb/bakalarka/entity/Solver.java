package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Solver {
    private int chosenGame;

    public Solver(int chosenGame) {
        this.chosenGame = chosenGame;
    }

    public boolean isSolved(Task task) {
        switch (chosenGame) {
            case 1:
            case 2:
                return solverGame_1_2(task);
            case 3:
                return solver3game(task);
        }
        return false;

    }

    private boolean solver3game(Task task) {
        return false;
    }

    private boolean solverGame_1_2(Task task) {
        ArrayList<Animal> left = task.getLeftSide();
        ArrayList<Animal> right = task.getRightSide();
        Operation operation = task.getOperation();
        int leftSum = 0;
        int rightSum = 0;
        for (Animal animal : left) {
            leftSum += animal.getValue();
        }
        for (Animal animal : right) {
            rightSum += animal.getValue();
        }
        return compare(leftSum, rightSum, operation);
    }

    private boolean compare(int leftSum, int rightSum, Operation operation) {
        boolean result = false;
        Log.e("SOLVER", leftSum + " " + operation + " " + rightSum);
        switch (operation) {
            case EQUAL:
                result = leftSum == rightSum;
                break;
            case GREATER_THAN:
                result = leftSum > rightSum;
                break;
            case LESS_THAN:
                result = leftSum < rightSum;
                break;
            case NOT_EQUAL:
                result = leftSum != rightSum;
                break;
        }
        Log.e("SOLVER", leftSum + " " + operation + " " + rightSum + " = " + result);
        return result;
    }

    public int getNumberOfSolutions( Task task, boolean useAllAnimals) {
        switch (chosenGame) {
            case 1:
            case 2:
                return getNumberOfSolutionGame_1_2(task, useAllAnimals);
            case 3:
                // TODO doorbit solver get number of solutions game 3
                return 0;
            case 4:
                return getNumberOfSolutionGame_4(task, useAllAnimals);
        }
        return 0;
    }

    public int getNumberOfSolutionGame_1_2(Task task, boolean useAllAnimals) {
        int res = 0;
        int leftSum = 0;
        int rightSum = 0;
        boolean emptyOnLeftSide = false;
        for (Animal animal : task.getLeftSide()) {
            if (animal == Animal.EMPTY) {
                emptyOnLeftSide = true;
            }
            leftSum += animal.getValue();
        }

        for (Animal animal : task.getRightSide()) {
            rightSum += animal.getValue();
        }

        int temp = 0;
        // for Inequations - ONE EMPTY PLACE

        // TODO dorobit
        // Log.e("SOLVER","left= "+leftSum+" right= "+rightSum);
        if (useAllAnimals) {
            for (Animal animal : Animal.hard)
                if (emptyOnLeftSide) {
                    temp = rightSum - leftSum;
                    if (compare(animal.getValue(), temp, task.getOperation())) {
                        res++;
                        //  Log.e("SOLVER", "LEFT 1, SOLVABLE");

                    }
                } else {
                    temp = leftSum - rightSum;
                    if (compare(temp, animal.getValue(), task.getOperation())) {
                        res++;
                        //  Log.e("SOLVER", "RIGHT, SOLVABLE");
                    }
                }
        } else {
            for (Animal animal : Animal.easy)
                if (emptyOnLeftSide) {
                    temp = rightSum - leftSum;
                    if (compare(animal.getValue(), temp, task.getOperation())) {
                        res++;
                        //   Log.e("SOLVER", "LEFT 2, SOLVABLE");

                    }
                } else {
                    temp = leftSum - rightSum;
                    if (compare(temp, animal.getValue(), task.getOperation())) {
                        res++;
                        //   Log.e("SOLVER", "RIGHT, SOLVABLE");
                    }
                }
        }


        return res;

    }

    public int getNumberOfSolutionGame_3(Task task, boolean useAllAnimals) {
        return 0;
    }

    public int getNumberOfSolutionGame_4(Task task, boolean useAllAnimals) {
        ArrayList<Animal> leftSide = task.getLeftSide();
        ArrayList<Animal> rightSide = task.getRightSide();
        int leftSum = 0;
        int rightSum = 0;
        int left_x = 0;
        int left_y = 0;

        int right_x = 0;
        int right_y = 0;
        for (Animal animal : leftSide) {
            leftSum += animal.getValue();
            if (animal == Animal.EMPTY) {
                left_x++;
            }
            if (animal == Animal.EMPTY2) {
                left_y++;
            }
        }

        for (Animal animal : rightSide) {
            rightSum += animal.getValue();
            if (animal == Animal.EMPTY) {
                right_x++;
            }
            if (animal == Animal.EMPTY2) {
                right_y++;
            }
        }
        int solutions = 0;
        Animal[] animals = (useAllAnimals == true) ? Animal.hard : Animal.easy;
        for (Animal animalX : animals) {
            for (Animal animalY : animals) {
                if (animalX != animalY) {
                    int left = getSum(left_x, left_y, leftSum, animalX, animalY);
                    int right = getSum(right_x, right_y, rightSum, animalX, animalY);
                    if (compare(left, right, Operation.EQUAL)) {
                        solutions++;
                    }
                }
            }
        }
        return solutions;

    }

    private int getSum(int x, int y, int sum, Animal animalX, Animal animalY) {
        return (x*animalX.getValue()) + (y*animalY.getValue()) + sum;
    }


}