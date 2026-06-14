package com.example.fingame;

public class Meld {

    int score;
    String description;

    public Meld(int score, String description) {
        this.score = score;
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public String getDescription() {
        return description;
    }
}
