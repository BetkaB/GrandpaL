package com.example.beebzb.bakalarka.entity;

import android.util.Log;

import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
            case 4:
                return solverGame_4(task);
        }
        return false;

    }

    private boolean solverGame_4(Task task) {
        HashSet<Animal> var_x = task.getVariable_x();
        HashSet<Animal> var_y = task.getVariable_y();

        if (var_x == null|| var_y == null) return false;

        Log.e("SOLVER","X : "+var_x.toString());
        Log.e("SOLVER","Y : "+var_y.toString());


        if (var_x.size() != 1) return false;
        if (task.getChosenLevel() != 1 && var_y.size() != 1 ) return  false;    // if level is 2 or 3 and var_y is empty or more animals
        if (var_x.contains(null) || var_y.contains(null)) return false;

        Animal x = null;
        Animal y = null;
        for (Animal animal : var_x){x = animal;}
        for (Animal animal : var_y){y = animal;}
        if (x == y) return false;

        ArrayList<Animal> leftSide = task.getLeftSide();
        ArrayList<Animal> rightSide = task.getRightSide();
        int leftSum = 0;
        int rightSum = 0;

        for (Animal animal : leftSide){
            if (animal == Animal.EMPTY){
                leftSum += x.getValue();
            }
            else if (animal == Animal.EMPTY2){
                leftSum += y.getValue();
            }
            else {leftSum += animal.getValue();}
        }

        for (Animal animal : rightSide){
            if (animal == Animal.EMPTY){
                rightSum += x.getValue();
            }
            else if (animal == Animal.EMPTY2){
                rightSum += y.getValue();
            }
            else {rightSum += animal.getValue();}
        }


        return leftSum == rightSum;
    }

    private boolean solver3game(Task task) {
        int parts = task.getFence().getParts();

        int left = getSumOfArray(task.getLeftPlace());
        int right = getSumOfArray(task.getRightPlace());
        int totalAnimals = getNumberOfAnimals(task.getAnimalMap());
        if (parts == 2) {
            Log.e("SOLVER","FINALY : left= "+left+", right="+right+", total = "+totalAnimals + "usedAnimals = " + (task.getLeftPlace().size() + task.getRightPlace().size()));
            return (left == right) && (task.getLeftPlace().size() + task.getRightPlace().size() == totalAnimals);
        }
        if (parts == 3) {
            int middle = getSumOfArray(task.getMiddlePlace());
            Log.e("SOLVER","FINALY : left= "+left+", right="+right+", total = "+totalAnimals);
            return (left == right) && (left == middle )&& (task.getLeftPlace().size() + task.getRightPlace().size() + task.getMiddlePlace().size() == totalAnimals);
        }
        return false;
    }

    private int getNumberOfAnimals(HashMap<Animal, Integer> animalMap) {
        int res = 0;

        Iterator it = animalMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            res += (int) pair.getValue();
        }
        return  res;
    }

    private int getSumOfArray(ArrayList<Animal> array) {
        int res = 0;
        for (Animal animal : array) {
            res += animal.getValue();
        }
        return res;

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

    private boolean compare(int leftSum
            , int rightSum, Operation operation) {
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
       // Log.e("SOLVER", leftSum + " " + operation + " " + rightSum + " = " + result);
        return result;
    }

    public int getNumberOfSolutions(Task task, boolean useAllAnimals) {
        switch (chosenGame) {
            case 1:
                return 1;
            case 2:
                return getNumberOfSolutionGame_2(task, useAllAnimals);
            case 3:
                return getNumberOfSolutionGame_3(task);
            case 4:
                return getNumberOfSolutionGame_4(task, useAllAnimals);
        }
        return 0;
    }

    public int getNumberOfSolutionGame_2(Task task, boolean useAllAnimals) {
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

    public int getNumberOfSolutionGame_3(Task task) {
        if (task.getChosenLevel() == 3){
            return getNumberOfSolutions_3_game_3_level(task.getAnimalMap());
        }
        else {
            Integer[][] fences2 = new Integer[][]{new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}};
            Integer[][] fences3 = new Integer[][]{new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}, new Integer[]{0, 0, 0, 0, 0, 0, 0, 0}};
            Integer[][] fences = (task.getChosenLevel() == 1) ? fences2 : fences3;
            return solve_GAME_3(getArrayFromHashMap(task), fences);
        }
    }

    private Integer[] getArrayFromHashMap(Task task){
        Integer[] res = new Integer[] {0,0,0,0,0,0,0,0};
        List<Animal> animals = Arrays.asList(Animal.MOUSE, Animal.CAT, Animal.GOOSE, Animal.DOG, Animal.GOAT, Animal.RAM, Animal.COW, Animal.HORSE);

        HashMap hashMap = task.getAnimalMap();
        Iterator it = hashMap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            Animal animal = (Animal) pair.getKey();
            int index = animals.indexOf(animal);
            int numberOfAnimal = (int) pair.getValue();
            res[index] = numberOfAnimal;

        }
        return  res;
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
        return (x * animalX.getValue()) + (y * animalY.getValue()) + sum;
    }


    final static int values[] = { 1, 2, 3, 4, 5, 6,10, 20 };

    private int sum(Integer[] array) {
        int sum = 0;
        for (int i = 0; i < array.length; i++)
            sum += array[i] * values[i];
        return sum;
    }

    private int solve_GAME_3(Integer[] animals, Integer[][] fences) {
        int count = fences.length;
        int sum = sum(animals);
        if (sum % count != 0) {
            return 0 ;
        } else {
            HashSet<ArrayList<String>> solSet = new HashSet<ArrayList<String>>();
            solve_GAME_3(animals, fences, sum / count, solSet);
                /*for (ArrayList<String> solution : solSet) {
                }*/
            return solSet.size();
        }
    }

    private void solve_GAME_3_NO_RE(Integer[] animals, Integer[][] fences, int part, HashSet<ArrayList<String>> solSet) {
        while(solSet.size() < 1) {
            if (sum(animals) == 0) {            // No Animals left
                boolean ok = true;              // isOK
                for (Integer[] fence : fences)
                    if (sum(fence) != part) {   // No solution || Bad solution
                        ok = false;
                    }
                if (ok) {                       // Found solution
                    ArrayList<String> al = new ArrayList<String>();
                    for (Integer[] chliev : fences) {
                        String l = new String();
                        for (Integer ch : chliev) {
                            l += ch;
                        }
                        al.add(l);
                    }
                    // al.sort(null);
                    Collections.sort(al);
                    solSet.add(al);
                    return;                     // Return call
                }
            } else {                            // Animals left
                for (int a = 0; a < animals.length; a++) {
                    if (animals[a] > 0) {       // Contains animals
                        for (int f = 0; f < fences.length; f++) {       // Add to fences
                            animals[a]--;
                            fences[f][a]++;
                            if (sum(fences[f]) <= part) {
                                solve_GAME_3(animals, fences, part, solSet);
                            }
                            fences[f][a]--;
                            animals[a]++;
                        }
                    }
                }
            }
        }
    }

    private Integer[] hashMapToArray(HashMap<Animal, Integer> hmap) {
        ArrayList<Integer> temp = new ArrayList<>();
        Iterator it = hmap.entrySet().iterator();
        while (it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            for (int i = 0; i < (int)pair.getValue(); i++) {
                temp.add(((Animal)pair.getKey()).getValue());
            }
        }
        Integer[] result = new Integer[temp.size()];
        int idx = 0;
        for (Integer i : temp) {
            result[idx] = i;
            idx++;
        }
        return result;
    }

    private int  getNumberOfSolutions_3_game_3_level(HashMap<Animal, Integer> animalMap) {
        Integer[] animals = hashMapToArray(animalMap);
        Arrays.sort(animals, Collections.reverseOrder());
        ArrayList<ArrayList<Integer>> groups = new ArrayList<>();
        final int number_of_groups = 3;
        for (int i = 0; i < number_of_groups; i++) {
            groups.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < animals.length; i++) {
            ArrayList<Integer> mingroup = groups.get(0);
            for (ArrayList group : groups) {
                if (sumOfArrayList(group) < sumOfArrayList(mingroup)){
                    mingroup = group;
                }
            }
            mingroup.add(animals[i]);
        }
        int sumOfFirst = sumOfArrayList(groups.get(0));
        if ( sumOfFirst== sumOfArrayList(groups.get(1)) && sumOfArrayList(groups.get(2)) == sumOfFirst){
            return 1;
        }
        return 0;
    }

    private int sumOfArrayList(ArrayList<Integer> list){
        int sum = 0;
        for (Integer i : list){
            sum+= i;
        }
        return sum;
    }

    private void solve_GAME_3(Integer[] animals, Integer[][] fences, int part, HashSet<ArrayList<String>> solSet) {
        if (solSet.size() == 1){
            return;
        }
        if (sum(animals) == 0) { //al animals are
            boolean ok = true;
            for (Integer[] fence : fences)
                if (sum(fence) != part) {
                    ok = false;
                }
            if (ok) {
                ArrayList<String> al = new ArrayList<String>();
                for (Integer[] chliev : fences) {
                    String l = new String();
                    for (Integer ch : chliev) {
                        l += ch;
                    }
                    al.add(l);
                }
                // al.sort(null);
                Collections.sort(al);
                solSet.add(al);

            }
        } else {
            for (int a = 0; a < animals.length; a++) {
                if (animals[a] > 0) {
                    for (int f = 0; f < fences.length; f++) {
                        animals[a]--;
                        fences[f][a]++;
                        if (sum(fences[f]) <= part) {
                            solve_GAME_3(animals, fences, part, solSet);
                            if (solSet.size() > 0) return;
                        }
                        fences[f][a]--;
                        animals[a]++;
                    }
                }
            }
        }
    }


}