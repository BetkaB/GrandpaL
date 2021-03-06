package com.example.beebzb.bakalarka.entity.enums;

import com.example.beebzb.bakalarka.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Animal {
    MOUSE(1, R.drawable.mouse),
    CAT(2, R.drawable.cat),
    GOOSE(3, R.drawable.goose),
    DOG(4, R.drawable.dog),
    GOAT(5, R.drawable.goat),
    RAM(6, R.drawable.ram),
    COW(10, R.drawable.cow),
    HORSE(20, R.drawable.horse),
    EMPTY(0, R.drawable.empty),
    EMPTY2(0, R.drawable.empty2),
    EMPTY_INVISIBLE (0, R.drawable.empty4);

    private int value;
    private int drawable;

    public static final Animal[] easy = {MOUSE, CAT, GOOSE, DOG, GOAT, RAM};
    public static final Animal[] hard = {MOUSE, CAT, GOOSE, DOG, GOAT, RAM, COW, HORSE};


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

    private static final List<Animal> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static Animal randomAnimal() {
        return VALUES.get(RANDOM.nextInt(SIZE - 5));
    }

    public static Animal randomFromAllAnimals() {
        return VALUES.get(RANDOM.nextInt(SIZE - 3));
    }

    public static Animal getRandomAnimalBasedOnLevel(int chosenLevel) {
        switch (chosenLevel) {
            case 3:
                return randomFromAllAnimals();
            default:
                return randomAnimal();

        }
    }

}

