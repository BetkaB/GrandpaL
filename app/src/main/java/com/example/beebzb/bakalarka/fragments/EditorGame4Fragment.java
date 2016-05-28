package com.example.beebzb.bakalarka.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.beebzb.bakalarka.R;
import com.example.beebzb.bakalarka.entity.Generator;
import com.example.beebzb.bakalarka.entity.Solver;
import com.example.beebzb.bakalarka.entity.Task;
import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;
import com.example.beebzb.bakalarka.layout.MyDialog;
import com.example.beebzb.bakalarka.layout.MyNumberPicker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditorGame4Fragment extends Fragment {
    private View view;

    private final int CAN_BE_SAVED = 1;
    private final int TOO_FEW_ANIMALS = 2;
    private final int EVERY_VARIABLE_MUST_BE_USED = 3;
    private final int HAS_NO_SOLUTION = 4;
    private final int HAS_SOLUTION = 5;
    private final int TOO_MANY_VARIABLES = 6;
    private final int TOO_MANY_ANIMALS_OR_VARIABLES = 7;

    private Button hasSolutionButton;

    private Solver solver = new Solver(4);

    private MyNumberPicker leftMouse;
    private MyNumberPicker leftCat;
    private MyNumberPicker leftGoose;
    private MyNumberPicker leftDog;
    private MyNumberPicker leftGoat;
    private MyNumberPicker leftRam;
    private MyNumberPicker leftCow;
    private MyNumberPicker leftHorse;
    private MyNumberPicker left_x;
    private MyNumberPicker left_y;

    private MyNumberPicker[] leftPickers;


    private MyNumberPicker rightMouse;
    private MyNumberPicker rightCat;
    private MyNumberPicker rightGoose;
    private MyNumberPicker rightDog;
    private MyNumberPicker rightGoat;
    private MyNumberPicker rightRam;
    private MyNumberPicker rightCow;
    private MyNumberPicker rightHorse;
    private MyNumberPicker right_x;
    private MyNumberPicker right_y;

    private MyNumberPicker[] rightPickers;

    public static final String SAVED_TASK_GAME4 = "game4";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor_game4, container, false);
        initPickers();
        setListeners();
        return view;
    }


    private void setListeners() {
        hasSolutionButton = (Button) view.findViewById(R.id.hasSolutionButton);
        hasSolutionButton.setOnClickListener(new View.OnClickListener() {
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
        int solutions = solver.getNumberOfSolutions(task, true);
        return solutions > 0;
    }

    private Task createTaskFromLayout() {
        Task task = new Task(4, 3);
        task.setOperation(Operation.EQUAL);
        task.setLeftSide(getSideFromLayout(leftPickers));
        task.setRightSide(getSideFromLayout(rightPickers));

        // fill variables
        int temp = left_x.getNumber();
        for (int i = 0; i < temp; i++){
            task.addToLeftSide(Animal.EMPTY);
        }
        temp = left_y.getNumber();
        for (int i = 0; i < temp; i++){
            task.addToLeftSide(Animal.EMPTY2);
        }
        temp = right_x.getNumber();
        for (int i = 0; i < temp; i++){
            task.addToRightSide(Animal.EMPTY);
        }
        temp = right_y.getNumber();
        for (int i = 0; i < temp; i++){
            task.addToRightSide(Animal.EMPTY2);
        }

        Log.e("ED1", task.toString());
        return task;
    }

    private int countAnimalsOnSide(MyNumberPicker[] pickers) {
        int res = 0;
        for (MyNumberPicker picker : pickers) {
            res += picker.getNumber();
        }
        return res;
    }


    private void initPickers() {
        // left side
        leftMouse = (MyNumberPicker) view.findViewById(R.id.leftMouse);
        leftCat = (MyNumberPicker) view.findViewById(R.id.leftCat);
        leftGoose = (MyNumberPicker) view.findViewById(R.id.leftGoose);
        leftDog = (MyNumberPicker) view.findViewById(R.id.leftDog);
        leftGoat = (MyNumberPicker) view.findViewById(R.id.leftGoat);
        leftRam = (MyNumberPicker) view.findViewById(R.id.leftRam);
        leftCow = (MyNumberPicker) view.findViewById(R.id.leftCow);
        leftHorse = (MyNumberPicker) view.findViewById(R.id.leftHorse);
        left_x = (MyNumberPicker) view.findViewById(R.id.left_x);
        left_y = (MyNumberPicker) view.findViewById(R.id.left_y);

        leftPickers = new MyNumberPicker[]{leftMouse, leftCat, leftGoose, leftDog, leftGoat, leftRam, leftCow, leftHorse};
        //right side
        rightMouse = (MyNumberPicker) view.findViewById(R.id.rightMouse);
        rightCat = (MyNumberPicker) view.findViewById(R.id.rightCat);
        rightGoose = (MyNumberPicker) view.findViewById(R.id.rightGoose);
        rightDog = (MyNumberPicker) view.findViewById(R.id.rightDog);
        rightGoat = (MyNumberPicker) view.findViewById(R.id.rightGoat);
        rightRam = (MyNumberPicker) view.findViewById(R.id.rightRam);
        rightCow = (MyNumberPicker) view.findViewById(R.id.rightCow);
        rightHorse = (MyNumberPicker) view.findViewById(R.id.rightHorse);
        right_x = (MyNumberPicker) view.findViewById(R.id.right_x);
        right_y = (MyNumberPicker) view.findViewById(R.id.right_y);

        rightPickers = new MyNumberPicker[]{rightMouse, rightCat, rightGoose, rightDog, rightGoat, rightRam, rightCow, rightHorse};
    }

    private void showPopUp(int id) {
        Resources res = getActivity().getResources();
        switch (id) {
            case CAN_BE_SAVED:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_can_be_saved_title), res.getString(R.string.dialog_task_can_be_saved_info), true);
                break;
            case TOO_FEW_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_few_animals_4), false);
                break;
            case EVERY_VARIABLE_MUST_BE_USED:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_too_few_variables), false);
                break;
            case HAS_NO_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_no_solution_title), res.getString(R.string.dialog_task_has_no_solution_info), false);
                break;
            case HAS_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_solution_title), res.getString(R.string.dialog_task_has_solution_info), true);
                break;
            case TOO_MANY_VARIABLES:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_too_many_variables), false);
                break;
            case TOO_MANY_ANIMALS_OR_VARIABLES:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_too_many_variables_and_animals), false);
                break;
        }
    }

    private void save(Task task) {
        Gson gson = new Gson();
        String json = gson.toJson(task);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SAVED_TASK_GAME4, json).apply();
    }


    private int getSavingResult() {
        int animalsOnLeftSide = countAnimalsOnSide(leftPickers);
        int animalsOnRightSide = countAnimalsOnSide(rightPickers);
        int variable_x = left_x.getNumber() + right_x.getNumber();
        int variable_y = left_y.getNumber() + right_y.getNumber();
        if (variable_x == 0 || variable_y == 0) {
            return EVERY_VARIABLE_MUST_BE_USED;
        }
        if (variable_x + variable_y + animalsOnLeftSide + animalsOnRightSide < Generator.GAME_4_MIN_OF_ANIMALS) {
            return TOO_FEW_ANIMALS;
        }
        if (variable_x > Generator.GAME_4_MAX_OF_VARIABLE_LEVEL_2_3 || variable_y > Generator.GAME_4_MAX_OF_VARIABLE_LEVEL_2_3) {
            return TOO_MANY_VARIABLES;
        } else if (animalsOnLeftSide + animalsOnRightSide + variable_x + variable_y > Generator.MAX_OF_CIRCLES_IN_THIRD_LEVEL) {
            return TOO_MANY_ANIMALS_OR_VARIABLES;
        }
        Task task = createTaskFromLayout();
        if (hasSolution(task)) {
            save(task);
            return CAN_BE_SAVED;
        }
        return HAS_NO_SOLUTION;

    }

    public void onSaveClicked() {
        showPopUp(getSavingResult());
    }

    public ArrayList<Animal> getSideFromLayout(MyNumberPicker[] pickers) {
        ArrayList<Animal> side = new ArrayList<Animal>();
        for (int pic = 0; pic < pickers.length; pic++) {
            int count = pickers[pic].getNumber();
            for (int i = 0; i < count; i++) {
                side.add(Animal.hard[pic]);
            }
        }
        return side;
    }

}
