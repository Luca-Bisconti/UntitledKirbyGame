package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.engine.GameLoop;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.PassBinding;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.game.DemoSceneFactory;
import it.unicam.cs.mpgc.rpg130396.javaFX.Input.JavaFXInputAdapter;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class JavaFXLauncher extends Application {

    private GameLoop gameLoop;

    @Override
    public void start(Stage primaryStage) {
        Window window = new JavaFXWindow(primaryStage, "Kirby")
                .setWidth(1920).setHeight(1080).setResizable(false);

        PolledInput input = new PolledInput();
        new JavaFXInputAdapter(primaryStage.getScene(), input);
        primaryStage.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (!focused) {
                input.releaseAll();
            }
        });

        Scene scene = new DemoSceneFactory().create(
                JavaFXDemoTextures.createBackground(),
                JavaFXDemoTextures.createGround(),
                JavaFXDemoTextures.createDemoAtlas()
        );
        gameLoop = new GameLoop(window, createRenderer(window), scene, input);
        gameLoop.start();
    }

    @Override
    public void stop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }

    private static Renderer createRenderer(Window window) {
        RenderTarget backgroundTarget = new JavaFXRenderTarget(640, 360);
        RenderTarget groundTarget = new JavaFXRenderTarget(DemoSceneFactory.WIDTH, DemoSceneFactory.HEIGHT);
        RenderTarget characterTarget = new JavaFXRenderTarget(DemoSceneFactory.WIDTH, DemoSceneFactory.HEIGHT);
        RenderTarget screenTarget = window.getScreenTarget();
        Compositor compositor = new Compositor();
        return new Renderer(
                window.getDrawContext(),
                Map.of(0, backgroundTarget, 1, groundTarget, 2, characterTarget),
                screenTarget,
                DemoSceneFactory.WIDTH,
                DemoSceneFactory.HEIGHT,
                List.of(
                        new PassBinding(compositor, backgroundTarget, screenTarget, true),
                        new PassBinding(compositor, groundTarget, screenTarget, false),
                        new PassBinding(compositor, characterTarget, screenTarget, false)
                )
        );
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
