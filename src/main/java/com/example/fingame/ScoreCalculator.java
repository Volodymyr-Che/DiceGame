package com.example.fingame;

import java.util.ArrayList;

public class ScoreCalculator {

    int countOf(int faceValue, ArrayList<Die> diceToScore) {
        int total = 0;
        for (int i = 0; i < diceToScore.size(); i++) {
            if (diceToScore.get(i).getValue() == faceValue) total++;
        }
        return total;
    }

    public ArrayList<Meld> calculate(ArrayList<Die> diceToScore) {
        ArrayList<Meld> foundMelds = new ArrayList<>();

        for (int face = 1; face <= 6; face++) {
            int count = countOf(face, diceToScore);
            int baseScore = face * 100;
            if (face == 1) baseScore = 1000;

            if (count >= 3) {
                foundMelds.add(new Meld(baseScore, "Three " + face + "s"));
            }
        }

        if (countOf(1, diceToScore) < 3) {
            for (int i = 0; i < countOf(1, diceToScore); i++) {
                foundMelds.add(new Meld(100, "Single 1"));
            }
        }

        if (countOf(5, diceToScore) < 3) {
            for (int i = 0; i < countOf(5, diceToScore); i++) {
                foundMelds.add(new Meld(50, "Single 5"));
            }
        }

        return foundMelds;
    }

    public boolean isFarkle(ArrayList<Die> diceToScore) {
        if (diceToScore.isEmpty()) return false;
        return calculate(diceToScore).isEmpty();
    }

    public boolean dieScores(Die die, ArrayList<Die> unheldDice) {
        if (die.getValue() == 1 || die.getValue() == 5) return true;
        return countOf(die.getValue(), unheldDice) >= 3;
    }
}
