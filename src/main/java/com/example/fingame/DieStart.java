package com.example.fingame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        rollButton.setOnAction(e -> handleRoll());

        HBox diceRow = new HBox(8);
        diceRow.setPadding(new Insets(10));

        for (int i = 0; i < 6; i++) {
            dieCanvases[i] = new Canvas(100, 100);
            drawDie(dieCanvases[i].getGraphicsContext2D(), game.getTable().getDice().get(i));
            diceRow.getChildren().add(dieCanvases[i]);
        }

        HBox topBar = new HBox(20, scoreLabel, turnLabel);
        topBar.setPadding(new Insets(6));

        VBox root = new VBox(8, topBar, diceRow, rollButton, statusLabel);
        root.setPadding(new Insets(12));

        Scene scene = new Scene(root, 700, 260);
        stage.setTitle("Farkle");
        stage.setScene(scene);
        stage.show();
    }

    void handleRoll() {
        game.rollDice();
        for (int i = 0; i < 6; i++) {
            drawDie(dieCanvases[i].getGraphicsContext2D(), game.getTable().getDice().get(i));
        }

        if (game.isFarkle()) {
            game.handleFarkle();
            for (int i = 0; i < 6; i++) {
                drawDie(dieCanvases[i].getGraphicsContext2D(), game.getTable().getDice().get(i));
            }
            turnLabel.setText("Turn: 0");
            statusLabel.setText("Farkle! Turn lost.");
        } else {
            statusLabel.setText("Click a die to hold it");
        }
    }

    void drawDie(GraphicsContext gc, Die die) {
        double size = 90;
        double x = 5;
        double y = 5;

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 100, 100);
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, size, size);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
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