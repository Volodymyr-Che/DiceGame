package com.example.fingame;

import java.util.ArrayList;

public class GameController {

    Table table;
    Bank bank;
    ScoreCalculator scorer;
    int totalScore;
    String playerName;
    int winTarget = 2000;

    public GameController(String playerName) {
        this.playerName = playerName;
        table = new Table();
        bank = new Bank();
        scorer = new ScoreCalculator();
        totalScore = 0;
    }

    public void rollDice() {
        table.roll();
    }

    public ArrayList<Die> getUnheldDice() {
        ArrayList<Die> unheldDice = new ArrayList<>();
        for (int i = 0; i < table.getDice().size(); i++) {
            if (!table.getDice().get(i).isHeld()) {
                unheldDice.add(table.getDice().get(i));
            }
        }
        return unheldDice;
    }

    public ArrayList<Die> getHeldDice() {
        ArrayList<Die> heldDice = new ArrayList<>();
        for (int i = 0; i < table.getDice().size(); i++) {
            if (table.getDice().get(i).isHeld()) {
                heldDice.add(table.getDice().get(i));
            }
        }
        return heldDice;
    }

    public void scoreDice() {
        ArrayList<Meld> foundMelds = scorer.calculate(getHeldDice());
        for (int i = 0; i < foundMelds.size(); i++) {
            bank.addMeld(foundMelds.get(i));
        }
    }

    public void bankAndEndTurn() {
        totalScore = totalScore + bank.clear();
        table.resetHeld();
    }

    public void handleFarkle() {
        bank.clear();
        table.resetHeld();
    }

    public boolean isFarkle() {
        return scorer.isFarkle(getUnheldDice());
    }

    public boolean dieIsUseless(Die die) {
        return !scorer.dieScores(die, getUnheldDice());
    }

    public boolean isGameOver() {
        return totalScore >= winTarget;
    }

    public Table getTable() { return table; }
    public Bank getBank() { return bank; }
    public int getScore() { return totalScore; }
    public String getPlayerName() { return playerName; }
}
