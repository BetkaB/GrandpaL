package com.example.beebzb.bakalarka.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beebzb.bakalarka.R;
import com.example.beebzb.bakalarka.entity.Generator;
import com.example.beebzb.bakalarka.entity.Task;
import com.example.beebzb.bakalarka.entity.enums.Animal;
import com.example.beebzb.bakalarka.entity.enums.Operation;
import com.example.beebzb.bakalarka.layout.MyDialog;
import com.example.beebzb.bakalarka.layout.MyNumberPicker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class EditorGame1Fragment extends Fragment {
    private View view;

    private final int CAN_BE_SAVED = 1;
    private final int TOO_FEW_ANIMALS = 2;
    private final int TOO_MANY_ANIMALS = 3;

    private MyNumberPicker leftMouse;
    private MyNumberPicker leftCat;
    private MyNumberPicker leftGoose;
    private MyNumberPicker leftDog;
    private MyNumberPicker leftGoat;
    private MyNumberPicker leftRam;
    private MyNumberPicker leftCow;
    private MyNumberPicker leftHorse;

    MyNumberPicker[] leftPickers;

    private MyNumberPicker rightMouse;
    private MyNumberPicker rightCat;
    private MyNumberPicker rightGoose;
    private MyNumberPicker rightDog;
    private MyNumberPicker rightGoat;
    private MyNumberPicker rightRam;
    private MyNumberPicker rightCow;
    private MyNumberPicker rightHorse;

    MyNumberPicker[] rightPickers;

    public static final String SAVED_TASK_GAME1 = "game1";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_editor_game1, container, false);
        initPickers();
        return view;
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

    private int countAnimalsOnSide(MyNumberPicker[] pickers) {
        int res = 0;
        for (MyNumberPicker picker : pickers) {
            res += picker.getNumber();
        }
        return res;
    }

    private void showPopUp(int id) {
        Resources res = getActivity().getResources();
        switch (id) {
            case CAN_BE_SAVED:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_can_be_saved_title), res.getString(R.string.dialog_task_can_be_saved_info), true);
                break;
            case TOO_FEW_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_few_animals_1), false);
                break;
            case TOO_MANY_ANIMALS:
                new MyDialog(getActivity(), res.getString(R.string.dialog_task_cannot_be_saved_title), res.getString(R.string.dialog_task_cannot_be_saved_info_too_many_animals_12), false);
                break;
        }
    }

    private void save(Task task) {
        Gson gson = new Gson();
        String json = gson.toJson(task);
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(SAVED_TASK_GAME1, json).apply();
    }

    private int getSavingResult() {
        int animalsOnLeftSide = countAnimalsOnSide(leftPickers);
        int animalsOnRightSide = countAnimalsOnSide(rightPickers);
        if (animalsOnLeftSide == 0 || animalsOnRightSide == 0) {
            return TOO_FEW_ANIMALS;
        }
        if (animalsOnLeftSide + animalsOnRightSide > Generator.MAX_OF_CIRCLES_IN_THIRD_LEVEL) {
            return TOO_MANY_ANIMALS;
        }
        return CAN_BE_SAVED;
    }

    private Task createTaskFromLayout() {
        Task task = new Task(1, 3);
        task.setOperation(Operation.EMPTY);
        task.setLeftSide(getSideFromLayout(leftPickers));
        task.setRightSide(getSideFromLayout(rightPickers));
        Log.e("ED1", task.toString());
        return task;
    }

    public void onSaveClicked() {
        int res = getSavingResult();
        if (res == CAN_BE_SAVED) {
            save(createTaskFromLayout());
        }
        showPopUp(res);
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
