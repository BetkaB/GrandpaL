package com.example.beebzb.bakalarka;

public enum Animal {
    MOUSE(1,R.drawable.mouse),
    CAT(2,R.drawable.cat),
    GOOSE(3, R.drawable.goose),
    DOG(4, R.drawable.dog),
    GOAT(5, R.drawable.goat),
    RAM(6,R.drawable.ram),
    COW(10,R.drawable.cow),
    HORSE(20,R.drawable.horse);

    private int value;
    private int drawable;

    Animal(int value, int dr) {
        this.value = value;
        this.drawable = dr;
    }

    public int getValue() {
        return value;
    }
    public int getDrawableInt() {
        return drawable;
    }
}
