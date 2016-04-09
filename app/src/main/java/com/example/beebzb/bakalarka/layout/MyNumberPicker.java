package com.example.beebzb.bakalarka.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beebzb.bakalarka.R;

public class MyNumberPicker extends LinearLayout {

    private int number = 0;
    private final int MAX = 10;

    private Button plus;
    private Button minus;
    private TextView numberTextView;

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
        setListeners();
    }

    private void setListeners() {
        plus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == MAX) {
                    return;
                }
                number++;
                numberTextView.setText(number + "");
            }
        });

        minus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (number == 0) {
                    return;
                }
                number--;
                numberTextView.setText(number + "");
            }
        });
    }

    public MyNumberPicker(Context context) {
        super(context);
        initViews(context);
        setListeners();
    }

    private void initViews(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_number_picker, this);
        minus = (Button) this.findViewById(R.id.minus);
        numberTextView = (TextView) this.findViewById(R.id.number);
        plus = (Button) this.findViewById(R.id.plus);
    }

    public int getNumber() {
        return number;

    }
}
