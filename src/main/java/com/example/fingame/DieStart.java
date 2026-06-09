package com.example.fingame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

public class DieStart extends Application {

    private int value = 1;
    private final Random random = new Random();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(200, 200);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawDie(gc);

        // Handle click
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            value = random.nextInt(6) + 1;
            drawDie(gc);
            System.out.println("Rolled: " + value);
        });

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 220, 220);

        stage.setTitle("JavaFX Die");
        stage.setScene(scene);
        stage.show();
    }

    private void drawDie(GraphicsContext gc) {
        double size = 150;
        double x = 25;
        double y = 25;

        // Clear
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 200, 200);

        // Draw die background
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x, y, size, size, 20, 20);

        gc.setStroke(Color.BLACK);
        gc.strokeRoundRect(x, y, size, size, 20, 20);

        // Dot positions
        double cx = x + size / 2;
        double cy = y + size / 2;
        double offset = size / 4;

        gc.setFill(Color.BLACK);

        switch (value) {
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

    private void dot(GraphicsContext gc, double x, double y) {
        double r = 8;
        gc.fillOval(x - r, y - r, r * 2, r * 2);
    }
}
