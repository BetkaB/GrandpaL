package com.example.beebzb.bakalarka.entity;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.example.beebzb.bakalarka.MyCanvas;

public class Fence {
    private boolean isReady = false;
    private int parts;
    private Paint fencesPaint;
    private int left,right,top,bottom,partWidth;

    public Fence(int level) {
        switch (level) {
            case 1:
                this.parts = 2;
                break;
            default:
                this.parts = 3;
                break;
        }
        fencesPaint = new Paint();
        fencesPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        fencesPaint.setStyle(Paint.Style.STROKE);
        fencesPaint.setStrokeWidth(4);
        fencesPaint.setColor(Color.parseColor("#444444"));
    }

    public void resize(int canvasW, int canvasH) {
        int totalWidth = canvasW - (2* MyCanvas.FENCES_SIDE_PADDING);

        // Sizes
        left = MyCanvas.FENCES_SIDE_PADDING;
        top = MyCanvas.FENCES_TOP_BOTTOM_PADDING;
        right = canvasW - MyCanvas.FENCES_SIDE_PADDING;
        bottom = canvasH - MyCanvas.FENCES_TOP_BOTTOM_PADDING;

        // Fence width
        partWidth = totalWidth / parts;

        isReady = true;
    }

    public void draw(Canvas canvas) {
        if (!isReady) return;
        for (int part = 1; part < parts; part++) {
            int lineLeft = left + (partWidth * part);
            canvas.drawLine(lineLeft, top, lineLeft, bottom, fencesPaint);
        }
        canvas.drawRect(left, top, right, bottom, fencesPaint);
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }

    public int getPartWidth() {
        return partWidth;
    }

    public int getLeft() {
        return left;
    }

    public int getParts() {
        return parts;
    }

    /*
    private void drawFence() {

            // create INVISIBLE static circles in fences
            int oneFenceHeight = bottom - top;
            int radius;
            if (oneFenceHeight < oneFenceWidth) {
                radius = oneFenceHeight / 3 / 2;
            } else {
                radius = oneFenceWidth / 3 / 2;
            }

            int sidePadding = (oneFenceWidth - (6 * radius)) / 2;
            int bottomTopPadding = (oneFenceHeight - (6 * radius)) / 2;

            int x = left + radius;
            int y = top + bottomTopPadding + radius;

            for (int i = 0; i < numberOfFences; i++) {
                x = left + (oneFenceWidth * i) + sidePadding + radius;
                y = top + bottomTopPadding + radius;
                for (int j = 0; j < NUMBER_OF_STATIC_CIRCLES_INSIDE_ONE_FENCE; j++) {
                    //staticCircles.add(new Circle(x, y, radius, true, context, Animal.EMPTY, null));
                    x += +(2 * radius);
                    if (j % 3 == 2) {
                        y += +2 * radius;
                        x = left + (oneFenceWidth * i) + sidePadding + radius;
                    }
                }
            }
        }
    }
    */

}
