package com.example.beebzb.bakalarka.enums;

import com.example.beebzb.bakalarka.R;

public enum Operation {
    NOT_EQUAL(R.drawable.ne),
    EQUAL(R.drawable.e),
    GREATER_THAN(R.drawable.gt),
    LESS_THAN(R.drawable.lt),
    EMPTY(R.drawable.empty);

    private int drawable;

    Operation( int dr) {
        this.drawable = dr;
    }

    public int getDrawableInt() {
        return drawable;
    }
}
