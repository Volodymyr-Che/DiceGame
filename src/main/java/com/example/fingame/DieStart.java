package com.example.fingame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DieStart extends Application {

    GameController game = new GameController("Player 1");
    Canvas[] dieCanvases = new Canvas[6];

    Label scoreLabel  = new Label("Score: 0");
    Label turnLabel   = new Label("Turn: 0");
    Label statusLabel = new Label("Press Roll to begin");

    Button rollButton = new Button("Roll");
    Button bankButton = new Button("Bank Score");

    HBox tableRow = new HBox(8);
    HBox heldRow  = new HBox(8);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        bankButton.setDisable(true);
        rollButton.setOnAction(e -> handleRoll());
        bankButton.setOnAction(e -> handleBank());

        for (int i = 0; i < 6; i++) {
            dieCanvases[i] = new Canvas(100, 100);
            final int dieIndex = i;
            dieCanvases[i].addEventHandler(MouseEvent.MOUSE_CLICKED, ev -> clickDie(dieIndex));
        }

        tableRow.setPadding(new Insets(8));
        tableRow.setMinHeight(120);
        tableRow.getChildren().add(new Label("Rolled dice appear here"));

        heldRow.setPadding(new Insets(8));
        heldRow.setMinHeight(120);
        heldRow.getChildren().add(new Label("Held dice appear here"));

        HBox topBar = new HBox(20, scoreLabel, turnLabel);
        topBar.setPadding(new Insets(6));

        Label tableTitle = new Label("Table");
        Label heldTitle  = new Label("Held");

        HBox buttonRow = new HBox(10, rollButton, bankButton);
        buttonRow.setPadding(new Insets(6));

        VBox root = new VBox(8, topBar, tableTitle, tableRow, heldTitle, heldRow, buttonRow, statusLabel);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 700, 420);
        stage.setTitle("Farkle");
        stage.setScene(scene);
        stage.show();
    }

    void handleRoll() {
        game.rollDice();

        tableRow.getChildren().clear();
        for (int i = 0; i < 6; i++) {
            Die currentDie = game.getTable().getDice().get(i);
            if (!currentDie.isHeld()) {
                drawDie(dieCanvases[i].getGraphicsContext2D(), currentDie);
                tableRow.getChildren().add(dieCanvases[i]);
            }
        }

        if (tableRow.getChildren().isEmpty()) {
            tableRow.getChildren().add(new Label("No dice left"));
        }

        if (game.isFarkle()) {
            game.handleFarkle();
            tableRow.getChildren().clear();
            heldRow.getChildren().clear();
            tableRow.getChildren().add(new Label("Rolled dice appear here"));
            heldRow.getChildren().add(new Label("Held dice appear here"));
            turnLabel.setText("Turn: 0");
            statusLabel.setText("Farkle! Turn lost.");
            bankButton.setDisable(true);
        } else {
            statusLabel.setText("Click a die to hold it");
            bankButton.setDisable(false);
        }
    }

    void handleBank() {
        game.scoreDice();
        game.bankAndEndTurn();

        tableRow.getChildren().clear();
        heldRow.getChildren().clear();
        tableRow.getChildren().add(new Label("Rolled dice appear here"));
        heldRow.getChildren().add(new Label("Held dice appear here"));

        scoreLabel.setText("Score: " + game.getScore());
        turnLabel.setText("Turn: 0");
        bankButton.setDisable(true);

        if (game.isGameOver()) {
            statusLabel.setText("You win! Final score: " + game.getScore());
            rollButton.setDisable(true);
        } else {
            statusLabel.setText("Banked. Roll again!");
        }
    }

    void clickDie(int dieIndex) {
        Die clickedDie = game.getTable().getDice().get(dieIndex);

        if (clickedDie.isHeld()) {
            clickedDie.setHeld(false);
            drawDie(dieCanvases[dieIndex].getGraphicsContext2D(), clickedDie);
            heldRow.getChildren().remove(dieCanvases[dieIndex]);
            tableRow.getChildren().add(dieCanvases[dieIndex]);
            if (heldRow.getChildren().isEmpty()) {
                heldRow.getChildren().add(new Label("Held dice appear here"));
            }
            turnLabel.setText("Turn: " + game.getBank().getScore());
            statusLabel.setText("Unheld.");
            return;
        }

        if (game.dieIsUseless(clickedDie)) {
            statusLabel.setText(clickedDie.getValue() + " is not a member of any melds");
            return;
        }

        clickedDie.setHeld(true);
        drawDie(dieCanvases[dieIndex].getGraphicsContext2D(), clickedDie);
        tableRow.getChildren().remove(dieCanvases[dieIndex]);

        if (tableRow.getChildren().isEmpty()) {
            tableRow.getChildren().add(new Label("No dice left"));
        }

        for (int i = heldRow.getChildren().size() - 1; i >= 0; i--) {
            if (heldRow.getChildren().get(i) instanceof Label) {
                heldRow.getChildren().remove(i);
                break;
            }
        }
        heldRow.getChildren().add(dieCanvases[dieIndex]);

        turnLabel.setText("Turn: " + game.getBank().getScore());
        statusLabel.setText("Held. Keep holding or bank.");
    }

    void drawDie(GraphicsContext gc, Die die) {
        double size = 90;
        double x = 5;
        double y = 5;

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 100, 100);

        Color faceColor = Color.WHITE;
        Color borderColor = Color.BLACK;
        if (die.isHeld()) {
            faceColor = Color.LIGHTBLUE;
            borderColor = Color.STEELBLUE;
        }
        gc.setFill(faceColor);
        gc.setStroke(borderColor);
        gc.setLineWidth(2);
        gc.fillRect(x, y, size, size);
        gc.strokeRect(x, y, size, size);

        double cx = x + size / 2;
        double cy = y + size / 2;
        double offset = size / 4;

        gc.setFill(Color.BLACK);
        switch (die.getValue()) {
            case 1 -> dot(gc, cx, cy);
            case 2 -> {
                dot(gc, cx - offset, cy - offset);
                dot(gc, cx + offset, cy + offset);
            }
            case 3 -> {
                dot(gc, cx - offset, cy - offset);
                dot(gc, cx, cy);
                dot(gc, cx + offset, cy + offset);
            }
            case 4 -> {
                dot(gc, cx - offset, cy - offset);
                dot(gc, cx + offset, cy - offset);
                dot(gc, cx - offset, cy + offset);
                dot(gc, cx + offset, cy + offset);
            }
            case 5 -> {
                dot(gc, cx - offset, cy - offset);
                dot(gc, cx + offset, cy - offset);
                dot(gc, cx, cy);
                dot(gc, cx - offset, cy + offset);
                dot(gc, cx + offset, cy + offset);
            }
            case 6 -> {
                dot(gc, cx - offset, cy - offset);
                dot(gc, cx + offset, cy - offset);
                dot(gc, cx - offset, cy);
                dot(gc, cx + offset, cy);
                dot(gc, cx - offset, cy + offset);
                dot(gc, cx + offset, cy + offset);
            }
        }
    }

    void dot(GraphicsContext gc, double x, double y) {
        double r = 8;
        gc.fillOval(x - r, y - r, r * 2, r * 2);
    }
}
