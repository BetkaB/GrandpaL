package com.example.beebzb.bakalarka;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MyDialog extends Dialog {

    public MyDialog(Context context, String title, String info, boolean positive) {
        super(context);

        final Context context1 = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.show();
        this.setContentView(R.layout.my_dialog);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView titleTextView = (TextView) findViewById(R.id.dialogTitle);
        TextView infoTextView = (TextView) findViewById(R.id.dialogInfo);
        Button okButton = (Button) findViewById(R.id.okButton);

        if (positive) {
            titleTextView.setTextColor(context.getResources().getColor(R.color.dialog_title_text_color_positive));
        }
        titleTextView.setText(title);
        infoTextView.setText(info);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        final Dialog dialog = this;
        final int LIMIT = 10000;
        final int SECOND = 1000;

        new CountDownTimer(LIMIT, SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        }.start();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}