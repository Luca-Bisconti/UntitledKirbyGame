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

    static Texture createBackground() {
        return JavaFXTexture.solid(DemoSceneFactory.WIDTH, DemoSceneFactory.HEIGHT, 0xFF334C79);
    }

    static Texture createGround() {
        return JavaFXTexture.solid(DemoSceneFactory.WIDTH, 32, 0xFF517A58);
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

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        JavaFXTexture texture = new JavaFXTexture(canvas.snapshot(parameters, null));
        return new SpriteAtlas(texture, cellWidth, cellHeight, pivotX, pivotY);
    }
}
