package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.nodes.DrawList;
import it.unicam.cs.mpgc.rpg130396.core.rendering.*;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import it.unicam.cs.mpgc.rpg130396.javaFX.window.JavaFXWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class JavaFXLauncher extends Application {

    private Window gameWindow;

    @Override
    public void start(Stage primaryStage) {
        gameWindow = new JavaFXWindow(primaryStage, "Kirby").setResizable(false);

        TextureRegion sprite = new TextureRegion(
                JavaFXTexture.solid(64, 64, 0xFFFF69B4)
        );

        Transform2D transform = new Transform2D()
                .setPosition(400, 300);

        DrawList drawList = new DrawList();

        drawList.add(new DrawCall(
                sprite,
                null,
                transform,
                0,
                0
        ));

        Renderer renderer = new Renderer(
                gameWindow.getDrawContext(),
                gameWindow.getScreenTarget(),
                List.of()
        );

        gameWindow.setFrameListener(dt -> {
            transform.rotate((float) dt);

            drawList.clear();

            drawList.add(new DrawCall(
                    sprite,
                    null,
                    transform.copy(),
                    0,
                    0
            ));

            renderer.render(drawList);
        });

        gameWindow.open();
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}