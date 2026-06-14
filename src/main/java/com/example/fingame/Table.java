package com.example.fingame;

import java.util.ArrayList;

public class Table {

    ArrayList<Die> dice = new ArrayList<>();

    public Table() {
        for (int i = 0; i < 6; i++) {
            dice.add(new Die());
        }
    }

    public void roll() {
        for (int i = 0; i < dice.size(); i++) {
            if (!dice.get(i).isHeld()) {
                dice.get(i).roll();
            }
        }
    }

    public ArrayList<Die> getDice() {
        return dice;
    }

    public void resetHeld() {
        for (int i = 0; i < dice.size(); i++) {
            dice.get(i).setHeld(false);
        }
    }

    public boolean allHeld() {
        for (int i = 0; i < dice.size(); i++) {
            if (!dice.get(i).isHeld()) return false;
        }
        return true;
    }
}
