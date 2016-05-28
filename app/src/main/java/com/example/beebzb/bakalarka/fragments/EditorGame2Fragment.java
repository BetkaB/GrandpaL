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
import android.widget.RadioButton;

import com.example.beebzb.bakalarka.R;
import com.example.beebzb.bakalarka.entity.Generator;
import com.example.beebzb.bakalarka.entity.Solver;
import com.example.beebzb.bakalarka.entity.Task;
import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;
import com.example.beebzb.bakalarka.layout.MyDialog;
import com.example.beebzb.bakalarka.layout.MyNumberPicker;
import com.google.gson.Gson;

public class EditorGame2Fragment extends Fragment {
    private View view;

    private final int CAN_BE_SAVED = 1;
    private final int TOO_FEW_ANIMALS = 2;
    private final int TOO_MANY_ANIMALS = 3;
    private final int HAS_NO_SOLUTION = 4;
    private final int HAS_SOLUTION = 5;

    private final int NUMBER_OF_EMPTY_ANIMALS = 1;

    private RadioButton left;
    private RadioButton right;
    private Button hasSolutionButton;

    private Solver solver = new Solver(2);

    private MyNumberPicker leftMouse;
    private MyNumberPicker leftCat;
    private MyNumberPicker leftGoose;
    private MyNumberPicker leftDog;
    private MyNumberPicker leftGoat;
    private MyNumberPicker leftRam;
    private MyNumberPicker leftCow;
    private MyNumberPicker leftHorse;

    private MyNumberPicker[] leftPickers;


    private MyNumberPicker rightMouse;
    private MyNumberPicker rightCat;
    private MyNumberPicker rightGoose;
    private MyNumberPicker rightDog;
    private MyNumberPicker rightGoat;
    private MyNumberPicker rightRam;
    private MyNumberPicker rightCow;
    private MyNumberPicker rightHorse;

    private MyNumberPicker[] rightPickers ;

    public static final String SAVED_TASK_GAME2 = "game2";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor_game2, container, false);
        initPickers();
        setListeners();
        return view;
    }


    private void setListeners() {
        left = (RadioButton) view.findViewById(R.id.rbleft);
        right = (RadioButton) view.findViewById(R.id.rbright);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left.setChecked(true);
                right.setChecked(false);
            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                left.setChecked(false);
                right.setChecked(true);
            }
        });

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

    private boolean emptyOnLeftSide() {
        return left.isChecked();
    }

    private boolean hasSolution(Task task) {
        int solutions = solver.getNumberOfSolutionGame_2(task, true);
        return solutions > 0;
    }

    private Task createTaskFromLayout() {
        Task task = new Task(2, 3);
        task.setOperation(Operation.EQUAL);
        if (emptyOnLeftSide()) {
            task.addToLeftSide(Animal.EMPTY);
            task.setEmptyAnimalOnLeftSide(true);
        }
        else {
            task.addToRightSide(Animal.EMPTY);
        }
        fillTaskSideFromLayout(leftPickers, task, true);
        fillTaskSideFromLayout(rightPickers, task, false);

        Log.e("SOLVER", "editor " + task.toString());
        return task;
    }

    private void fillTaskSideFromLayout(MyNumberPicker[] pickers, Task task, boolean toLeftSide) {
        for (int pic = 0; pic < pickers.length; pic++) {
            int count = pickers[pic].getNumber();
            for (int i = 0; i < count; i++) {
                if (toLeftSide) {
                    task.addToLeftSide(Animal.hard[pic]);
                }
                else {
                    task.addToRightSide(Animal.hard[pic]);
                }
            }
        }
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

        rightPickers = new MyNumberPicker[]{rightMouse, rightCat, rightGoose, rightDog, rightGoat, rightRam, rightCow, rightHorse};
    }


    private void showPopUp(int id) {
        Resources res = getActivity().getResources();
        switch (id) {
            case CAN_BE_SAVED:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_can_be_saved_title), res.getString(R.string.dialog_task_can_be_saved_info), true);
                break;
            case TOO_FEW_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_few_animals_2), false);
                break;
            case TOO_MANY_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_many_animals_12), false);
                break;
            case HAS_NO_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_no_solution_title), res.getString(R.string.dialog_task_has_no_solution_info), false);
                break;
            case HAS_SOLUTION:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_has_solution_title), res.getString(R.string.dialog_task_has_solution_info), true);
                break;
        }
    }

    private void save(Task task) {
        Gson gson = new Gson();
        String json = gson.toJson(task);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SAVED_TASK_GAME2, json).apply();
    }


    private int getSavingResult() {
        int animalsOnLeftSide = countAnimalsOnSide(leftPickers);
        int animalsOnRightSide = countAnimalsOnSide(rightPickers);
        if (animalsOnLeftSide == 0 && animalsOnRightSide == 0) {
            return TOO_FEW_ANIMALS;
        } else if (animalsOnLeftSide + animalsOnRightSide + NUMBER_OF_EMPTY_ANIMALS > Generator.MAX_OF_CIRCLES_IN_THIRD_LEVEL) {
            return TOO_MANY_ANIMALS;
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

}
