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
        return result;
    }
}
