package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.game.DemoSceneFactory;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


final class JavaFXDemoTextures {

    private JavaFXDemoTextures() {}

    static Texture createDistantBackground() {
        Canvas canvas = new Canvas(DemoSceneFactory.WORLD_WIDTH, DemoSceneFactory.HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#334C79"));
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.web("#667DA3"));
        graphics.fillPolygon(
                new double[]{0, 69, 133, 217, 309, 393, 470, 553, 629, 720, 720, 0},
                new double[]{89, 52, 83, 45, 94, 56, 81, 42, 79, 61, 135, 135}, 12);
        graphics.setFill(Color.web("#F5DAB0"));
        graphics.fillOval(390, 17, 19, 19);
        return snapshot(canvas);
    }

    static Texture createNearBackground() {
        Canvas canvas = new Canvas(DemoSceneFactory.WORLD_WIDTH, DemoSceneFactory.HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#355C60"));
        graphics.fillPolygon(
                new double[]{0, 56, 127, 205, 269, 353, 431, 502, 591, 652, 720, 720, 0},
                new double[]{110, 83, 107, 67, 98, 76, 111, 68, 92, 76, 105, 135, 135}, 13);
        return snapshot(canvas);
    }

    static Texture createGround() {
        Canvas canvas = new Canvas(DemoSceneFactory.WORLD_WIDTH, 32);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#517A58"));
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        graphics.setFill(Color.web("#89AC69"));
        graphics.fillRect(0, 0, canvas.getWidth(), 3);
        graphics.setFill(Color.web("#3D6249"));
        for (int x = 12; x < DemoSceneFactory.WORLD_WIDTH; x += 32) {
            graphics.fillRect(x, 10, 8, 3);
        }
        return snapshot(canvas);
    }

    static SpriteAtlas createDemoAtlas() {
        int cellWidth = 30;
        int cellHeight = 22;
        int pivotX = cellWidth / 2;
        int pivotY = cellHeight / 2;
        int frameCount = 3;
        Canvas canvas = new Canvas(cellWidth * frameCount, cellHeight);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        for (int frame = 0; frame < frameCount; frame++) {
            int centerX = frame * cellWidth + pivotX;
            int halfWidth = 9 + frame * 2;
            int halfHeight = 9 - frame * 2;
            graphics.setFill(Color.rgb(255, 131, 179, 0.8));
            graphics.fillPolygon(
                    new double[]{centerX, centerX + halfWidth, centerX, centerX - halfWidth},
                    new double[]{pivotY - halfHeight, pivotY, pivotY + halfHeight, pivotY}, 4);
            graphics.setFill(Color.rgb(255, 226, 238));
            graphics.fillRect(centerX - 2, pivotY - 2, 4, 4);
        }

        return new SpriteAtlas(snapshot(canvas), cellWidth, cellHeight, pivotX, pivotY);
    }

    private static Texture snapshot(Canvas canvas) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return new JavaFXTexture(canvas.snapshot(parameters, null));
    }
}
