
package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.PassBinding;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.game.PlayerController;
import it.unicam.cs.mpgc.rpg130396.javaFX.Input.JavaFXInputAdapter;
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

    private static final int GROUND_HEIGHT = 32;
    private static final int CHARACTER_HALF_WIDTH = 13;
    private static final int CHARACTER_FOOT_OFFSET = 9;

    private Window gameWindow;

    @Override
    public void start(Stage primaryStage) {

        gameWindow = new JavaFXWindow(
                primaryStage,
                "Kirby"
        ).setWidth(1920).setHeight(1080).setResizable(false);

        PolledInput input = new PolledInput();
        new JavaFXInputAdapter(primaryStage.getScene(), input);
        primaryStage.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (!focused) {
                input.releaseAll();
            }
        });

        Node root = new Node("Root") {};

        Sprite background = solidSprite("Background", 0, LOGICAL_WIDTH, LOGICAL_HEIGHT, 0xFF334C79);
        background.setLocal(background.getLocal().withPosition(-LOGICAL_WIDTH / 2f, -LOGICAL_HEIGHT / 2f));

        root.addChild(background);

        float groundTop = LOGICAL_HEIGHT / 2f - GROUND_HEIGHT;
        Sprite ground = solidSprite("Ground", 1, LOGICAL_WIDTH, GROUND_HEIGHT, 0xFF517A58);
        ground.setLocal(ground.getLocal().withPosition(-LOGICAL_WIDTH / 2f, groundTop));
        root.addChild(ground);

        Sprite character = new Sprite("Character");
        character.setLayer(2);
        float playerGroundY = groundTop - CHARACTER_FOOT_OFFSET;
        character.setLocal(character.getLocal().withPosition(0, playerGroundY));
        SpriteAnimator animator = new SpriteAnimator(character);
        SpriteAtlas atlas = createDemoAtlas();
        SpriteAnimation idle = new SpriteAnimation(List.of(atlas.frame(0)), 0.125, true);
        SpriteAnimation walk = new SpriteAnimation(
                List.of(atlas.frame(0), atlas.frame(1), atlas.frame(2), atlas.frame(1)), 0.125, true);
        float horizontalLimit = LOGICAL_WIDTH / 2f - CHARACTER_HALF_WIDTH;
        character.addComponent(new PlayerController(character, animator, idle, walk,
                -horizontalLimit, horizontalLimit, playerGroundY));
        character.addComponent(animator);
        root.addChild(character);

        Camera camera = new Camera("MainCamera", LOGICAL_WIDTH, LOGICAL_HEIGHT);
        Scene scene = new Scene(root, camera, true);
        RenderTarget backgroundTarget = new JavaFXRenderTarget(640, 360);
        RenderTarget groundTarget = new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT);
        RenderTarget characterTarget = new JavaFXRenderTarget(LOGICAL_WIDTH, LOGICAL_HEIGHT);
        RenderTarget screenTarget = gameWindow.getScreenTarget();
        Compositor compositor = new Compositor();
        Renderer renderer = new Renderer(
                gameWindow.getDrawContext(),
                Map.of(0, backgroundTarget, 1, groundTarget, 2, characterTarget),
                screenTarget,
                LOGICAL_WIDTH,
                LOGICAL_HEIGHT,
                List.of(
                        new PassBinding(compositor, backgroundTarget, screenTarget, true),
                        new PassBinding(compositor, groundTarget, screenTarget, false),
                        new PassBinding(compositor, characterTarget, screenTarget, false)
                )
        );

        gameWindow.setFrameListener(dt -> {
            scene.processInput(input);
            scene.update(dt);
            input.endFrame();
            renderer.render(scene.collectDrawList());
        });

        gameWindow.open();
    }

    private static SpriteAtlas createDemoAtlas() {
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
