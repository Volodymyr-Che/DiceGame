package com.example.fingame;

import java.util.Random;

public class Die {

    int value;
    boolean held;
    Random random = new Random();

    public Die() {
        value = 1;
        held = false;
    }

    public void roll() {
        value = random.nextInt(6) + 1;
    }

    public int getValue() {
        return value;
    }

    public void setHeld(boolean isHeld) {
        held = isHeld;
    }

    public boolean isHeld() {
        return held;
    }
}
