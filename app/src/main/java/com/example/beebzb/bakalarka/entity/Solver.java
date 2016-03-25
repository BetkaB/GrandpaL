package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.enums.Operation;

import java.util.ArrayList;

public class Solver {
    public boolean isSolved(Task task) {
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
        Log.e("SOLVER",leftSum+" "+operation+" "+rightSum);
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
        Log.e("SOLVER",leftSum+" "+operation+" "+rightSum+" = "+result);
        return result;
    }

    public int getNumberOfSolution(Task task, boolean useAllAnimals) {
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


}
