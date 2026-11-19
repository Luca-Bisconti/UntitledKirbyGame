
package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;

import javafx.application.Application;
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

        Sprite background = solidSprite("Background", 0, LOGICAL_WIDTH, LOGICAL_HEIGHT, 0xFF334C79);
        background.setLocal(background.getLocal().withPosition(-LOGICAL_WIDTH / 2f, -LOGICAL_HEIGHT / 2f));

        Sprite ground = solidSprite("Ground", 1, LOGICAL_WIDTH, 32, 0xFF517A58);
        ground.setLocal(ground.getLocal().withPosition(0, LOGICAL_HEIGHT - 32));
        background.addChild(ground);

        Sprite character = solidSprite("Character", 2, 30, 22, 0xCCFF83B3);
        character.getRegion().setOrigin(15, 11);
        character.setLocal(new Transform2D(120, 101, (float) Math.toRadians(30), 1));
        background.addChild(character);

        Camera camera = new Camera("MainCamera", LOGICAL_WIDTH, LOGICAL_HEIGHT);
        Scene scene = new Scene(background, camera, true);
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
            scene.update();
            renderer.render(scene.collectDrawList());
        });

        gameWindow.open();
    }

    private static Sprite solidSprite(String name, int layer, int width, int height, int argb) {
        TextureRegion region = new TextureRegion(JavaFXTexture.solid(width, height, argb));
        Sprite sprite = new Sprite(name).setRegion(region);
        sprite.layer = layer;
        return sprite;
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
