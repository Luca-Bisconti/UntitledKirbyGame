package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.engine.GameLoop;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.game.DemoSceneFactory;
import it.unicam.cs.mpgc.rpg130396.javaFX.Input.JavaFXInputAdapter;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.List;

public class JavaFXLauncher extends Application {

    private Window window;
    private AnimationTimer timer;

    @Override
    public void start(Stage primaryStage) {
        window = new JavaFXWindow(primaryStage, "Demo 2D")
                .setWidth(1920).setHeight(1080).setResizable(false);

        PolledInput input = new PolledInput();
        new JavaFXInputAdapter(primaryStage.getScene(), input);
        primaryStage.focusedProperty().addListener((observable, wasFocused, focused) -> {
            if (!focused) {
                input.releaseAll();
            }
        });

        Renderer renderer = new Renderer(window.getDrawContext(), window.getScreenTarget(), List.of());
        Scene scene = DemoSceneFactory.create(renderer, window.getScreenTarget());
        GameLoop gameLoop = new GameLoop(renderer, scene, input);
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameLoop.update(now);
            }
        };
        primaryStage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> timer.stop());
        window.open();
        timer.start();
    }

    @Override
    public void stop() {
        if (timer != null) {
            timer.stop();
        }
        if (window != null) {
            window.close();
        }
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
