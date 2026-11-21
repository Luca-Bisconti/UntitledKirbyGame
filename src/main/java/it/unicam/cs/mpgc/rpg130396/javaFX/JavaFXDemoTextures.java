package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.game.DemoAssets;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;

import static it.unicam.cs.mpgc.rpg130396.game.DemoConfig.*;


final class JavaFXDemoTextures {

    private JavaFXDemoTextures() {}

    private static final int ART_WIDTH = 720;
    private static final int ART_HEIGHT = 135;

    static DemoAssets create() {
        SpriteAtlas atlas = createDemoAtlas();
        SpriteAnimation idle = new SpriteAnimation(List.of(atlas.frame(0)), 0.125, true);
        SpriteAnimation walk = new SpriteAnimation(
                List.of(atlas.frame(0), atlas.frame(1), atlas.frame(2), atlas.frame(1)), 0.125, true);
        return new DemoAssets(List.of(
                new DemoAssets.Background("DistantBackground", createDistantBackground(), 0, 0, 0.2f, true),
                new DemoAssets.Background("NearBackground", createNearBackground(), 0, 0, 0.6f, true)
        ), createBridge(), idle, walk, null);
    }

    private static Canvas backgroundCanvas() {
        Canvas canvas = new Canvas(WORLD_WIDTH, LOGICAL_HEIGHT);
        canvas.getGraphicsContext2D().scale(WORLD_WIDTH / ART_WIDTH, (double) LOGICAL_HEIGHT / ART_HEIGHT);
        return canvas;
    }

    private static Texture createDistantBackground() {
        Canvas canvas = backgroundCanvas();
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#334C79"));
        graphics.fillRect(0, 0, ART_WIDTH, ART_HEIGHT);
        graphics.setFill(Color.web("#667DA3"));
        graphics.fillPolygon(
                new double[]{0, 69, 133, 217, 309, 393, 470, 553, 629, 720, 720, 0},
                new double[]{89, 52, 83, 45, 94, 56, 81, 42, 79, 61, 135, 135}, 12);
        graphics.setFill(Color.web("#FFF0CA"));
        graphics.fillOval(390, 17, 19, 19);
        graphics.fillRect(285, 22, 3, 3);
        graphics.fillRect(338, 33, 3, 3);
        graphics.fillRect(460, 26, 3, 3);
        return snapshot(canvas);
    }

    private static Texture createNearBackground() {
        Canvas canvas = backgroundCanvas();
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#355C60"));
        graphics.fillPolygon(
                new double[]{0, 56, 127, 205, 269, 353, 431, 502, 591, 652, 720, 720, 0},
                new double[]{110, 83, 107, 67, 98, 76, 111, 68, 92, 76, 105, 135, 135}, 13);
        return snapshot(canvas);
    }

    private static Texture createBridge() {
        Canvas canvas = new Canvas(BRIDGE_TEXTURE_WIDTH, BRIDGE_TEXTURE_HEIGHT);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.web("#517A58"));
        graphics.fillRect(0, BRIDGE_WALKABLE_Y, BRIDGE_TEXTURE_WIDTH, BRIDGE_TEXTURE_HEIGHT - BRIDGE_WALKABLE_Y);
        graphics.setFill(Color.web("#89AC69"));
        graphics.fillRect(0, BRIDGE_WALKABLE_Y, BRIDGE_TEXTURE_WIDTH, 3);
        graphics.setFill(Color.web("#3D6249"));
        for (int x = 12; x < BRIDGE_TEXTURE_WIDTH; x += 32) {
            graphics.fillRect(x, BRIDGE_WALKABLE_Y + 10, 8, 3);
        }
        return snapshot(canvas);
    }

    private static SpriteAtlas createDemoAtlas() {
        int cellWidth = 30;
        int cellHeight = 22;
        int pivotX = cellWidth / 2;
        int feetY = 20;
        int frameCount = 3;
        Canvas canvas = new Canvas(cellWidth * frameCount, cellHeight);
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        for (int frame = 0; frame < frameCount; frame++) {
            int centerX = frame * cellWidth + pivotX;
            int halfWidth = 9 + frame * 2;
            int halfHeight = 9 - frame * 2;
            int centerY = feetY - halfHeight;
            graphics.setFill(Color.rgb(255, 131, 179, 0.8));
            graphics.fillPolygon(
                    new double[]{centerX, centerX + halfWidth, centerX, centerX - halfWidth},
                    new double[]{centerY - halfHeight, centerY, feetY, centerY}, 4);
            graphics.setFill(Color.rgb(255, 226, 238));
            graphics.fillRect(centerX - 2, centerY - 2, 4, 4);
        }

        return new SpriteAtlas(snapshot(canvas), cellWidth, cellHeight, pivotX, feetY);
    }

    private static Texture snapshot(Canvas canvas) {
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        return new JavaFXTexture(canvas.snapshot(parameters, null));
    }
}
