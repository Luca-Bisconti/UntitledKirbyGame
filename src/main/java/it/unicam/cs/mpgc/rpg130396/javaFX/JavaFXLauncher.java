
package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;

import javafx.application.Application;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class JavaFXLauncher extends Application {

    private static final int LOGICAL_WIDTH = 240;
    private static final int LOGICAL_HEIGHT = 135;

    private Window gameWindow;

    @Override
    public void start(Stage primaryStage) {

        gameWindow = new JavaFXWindow(
                primaryStage,
                "Kirby"
        ).setWidth(1920).setHeight(1080).setResizable(false);

        Node root = new Node("Root") {};

        Sprite background = solidSprite("Background", 0, LOGICAL_WIDTH, LOGICAL_HEIGHT, 0xFF334C79);
        background.setLocal(background.getLocal().withPosition(-LOGICAL_WIDTH / 2f, -LOGICAL_HEIGHT / 2f));

        root.addChild(background);

        Sprite ground = solidSprite("Ground", 1, LOGICAL_WIDTH, 32, 0xFF517A58);
        ground.setLocal(ground.getLocal().withPosition(-LOGICAL_WIDTH / 2f, LOGICAL_HEIGHT / 2f - 32));
        root.addChild(ground);

        Sprite character = new Sprite("Character");
        character.setLayer(2);
        character.setLocal(new Transform2D(0, 101 - LOGICAL_HEIGHT / 2f, (float) Math.toRadians(30), 1));
        SpriteAnimator animator = new SpriteAnimator(character);
        character.addComponent(animator);
        animator.play(createDemoAnimation());
        root.addChild(character);

        Camera camera = new Camera("MainCamera", LOGICAL_WIDTH, LOGICAL_HEIGHT);
        Scene scene = new Scene(root, camera, true);
        Map<Integer, RenderTarget> targets = Map.of(
                0, new JavaFXRenderTarget(640, 360),
                1, new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT),
                2, new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT)
        );

        Renderer renderer = new Renderer(
                gameWindow.getDrawContext(),
                targets,
                gameWindow.getScreenTarget(),
                LOGICAL_WIDTH,
                LOGICAL_HEIGHT,
                List.of()
        );

        gameWindow.setFrameListener(dt -> {
            scene.update(dt);
            renderer.render(scene.collectDrawList());
        });

        gameWindow.open();
    }

    private static SpriteAnimation createDemoAnimation() {
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
        SpriteAtlas atlas = new SpriteAtlas(texture, cellWidth, cellHeight, pivotX, pivotY);
        return new SpriteAnimation(List.of(atlas.frame(0), atlas.frame(1), atlas.frame(2), atlas.frame(1)),
                0.125, true);
    }

    private static Sprite solidSprite(String name, int layer, int width, int height, int argb) {
        TextureRegion region = new TextureRegion(JavaFXTexture.solid(width, height, argb));
        Sprite sprite = new Sprite(name).setRegion(region);
        sprite.setLayer(layer);
        return sprite;
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
