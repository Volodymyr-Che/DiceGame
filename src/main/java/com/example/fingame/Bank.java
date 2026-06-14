package com.example.fingame;

import java.util.ArrayList;

public class Bank {

    ArrayList<Meld> melds = new ArrayList<>();

    public void addMeld(Meld meld) {
        melds.add(meld);
    }

    public int getScore() {
        int total = 0;
        for (int i = 0; i < melds.size(); i++) {
            total = total + melds.get(i).getScore();
        }
        return total;
    }

    public int clear() {
        int total = getScore();
        melds.clear();
        return total;
    }

    public ArrayList<Meld> getMelds() {
        return melds;
    }
}
