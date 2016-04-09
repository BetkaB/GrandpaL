package com.example.beebzb.bakalarka;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beebzb.bakalarka.entity.Generator;
import com.example.beebzb.bakalarka.entity.Solver;
import com.example.beebzb.bakalarka.entity.Task;
import com.example.beebzb.bakalarka.enums.Animal;
import com.example.beebzb.bakalarka.layout.MyNumberPicker;
import com.google.gson.Gson;

import java.util.HashMap;

public class EditorGame3Fragment extends Fragment {

    private View view;

    private MyNumberPicker npMouse;
    private MyNumberPicker npCat;
    private MyNumberPicker npGoose;
    private MyNumberPicker npDog;
    private MyNumberPicker npGoat;
    private MyNumberPicker npRam;
    private MyNumberPicker npCow;
    private MyNumberPicker npHorse;

    private MyNumberPicker[] npPickers;

    private final int CAN_BE_SAVED = 1;
    private final int TOO_FEW_ANIMALS = 2;
    private final int TOO_MANY_ANIMALS = 3;
    private final int HAS_NO_SOLUTION = 4;
    private final int HAS_SOLUTION = 5;

    private Solver solver = new Solver(3);

    public static final String SAVED_TASK_GAME3 = "game3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor_game3, container, false);
        initPickers();
        setListneres();
        return view;
    }

    private void setListneres() {
        Button hasSolutionsBtn = (Button) view.findViewById(R.id.hasSolutionButton);
        hasSolutionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluateTask();
            }
        });

    }

    private void evaluateTask() {
        Task task = createTaskFromLayout();

        if (hasSolution(task)) {
            showPopUp(HAS_SOLUTION);
        } else {
            showPopUp(HAS_NO_SOLUTION);
        }

    }

    private boolean hasSolution(Task task) {
        int solutions = solver.getNumberOfSolutionGame_3(task, true);
        return solutions > 0;
    }

    private void initPickers() {
        npMouse = (MyNumberPicker) view.findViewById(R.id.npMouse);
        npCat = (MyNumberPicker) view.findViewById(R.id.npCat);
        npGoose = (MyNumberPicker) view.findViewById(R.id.npGoose);
        npDog = (MyNumberPicker) view.findViewById(R.id.npDog);
        npGoat = (MyNumberPicker) view.findViewById(R.id.npGoat);
        npRam = (MyNumberPicker) view.findViewById(R.id.npRam);
        npCow = (MyNumberPicker) view.findViewById(R.id.npCow);
        npHorse = (MyNumberPicker) view.findViewById(R.id.npHorse);

        npPickers = new MyNumberPicker[]{npMouse, npCat, npGoose, npDog, npGoat, npRam, npCow, npHorse};
    }

    private void showPopUp(int id) {
        Resources res = getActivity().getResources();
        switch (id) {
            case CAN_BE_SAVED:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_can_be_saved_title), res.getString(R.string.dialog_task_can_be_saved_info), true);
                break;
            case TOO_FEW_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_few_animals_3), false);
                break;
            case TOO_MANY_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_many_animals_3), false);
                break;
            case HAS_NO_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_no_solution_title), res.getString(R.string.dialog_task_has_no_solution_info), false);
                break;
            case HAS_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_solution_title), res.getString(R.string.dialog_task_has_solution_info), true);
                break;
        }
    }

    public void onSaveClicked() {
        showPopUp(getSavingResult());
    }

    private int getSavingResult() {
        int animals = countAnimals(npPickers);
        if (animals < 3) {
            return TOO_FEW_ANIMALS;
        } else if (animals > Generator.GAME_3_MAX_OF_CIRCLES_LEVEL_3) {
            return TOO_MANY_ANIMALS;
        }
        Task task = createTaskFromLayout();
        if (hasSolution(task)) {
            save(task);
            return CAN_BE_SAVED;
        }
        return HAS_NO_SOLUTION;

    }

    private Task createTaskFromLayout() {
        Task task = new Task(3, 3);
        HashMap<Animal, Integer> animalMap = getHashMapFromLayout();
        task.setAnimalMap(animalMap);
        Log.e("ED1", task.toString());
        return task;
    }

    private HashMap<Animal, Integer> getHashMapFromLayout(){
        HashMap<Animal, Integer> map = new HashMap<>();
        for (int pic = 0; pic < npPickers.length; pic++){
            map.put(Animal.hard[pic],npPickers[pic].getNumber());
        }
        return map;

    }

    private int countAnimals(MyNumberPicker[] pickers) {
        int res = 0;
        for (MyNumberPicker picker : pickers) {
            res += picker.getNumber();
        }
        return res;
    }

    private void save(Task task) {
        SharedPreferences mPrefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(task);
        prefsEditor.putString(SAVED_TASK_GAME3, json);
        prefsEditor.commit();

    }


}
