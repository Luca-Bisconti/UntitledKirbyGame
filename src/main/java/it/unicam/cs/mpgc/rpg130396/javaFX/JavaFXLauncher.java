
package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
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

        gameWindow = new JavaFXWindow(
                primaryStage,
                "Kirby"
        ).setResizable(false);


        TextureRegion region = new TextureRegion(
                JavaFXTexture.solid(64, 64, 0xFFFF69B4)
        );

        region.originX = 32;
        region.originY = 32;


        Sprite sprite = new Sprite("TestSprite");
        sprite.setRegion(region);


        sprite.local.setPosition(0, 0).rotate(45);


        Camera camera = new Camera("MainCamera", 800, 600);


        Scene scene = new Scene(
                sprite,
                camera,
                true
        );


        Renderer renderer = new Renderer(
                gameWindow.getDrawContext(),
                gameWindow.getScreenTarget(),
                List.of()
        );

        gameWindow.setFrameListener(dt -> {

            sprite.local.rotate((float) dt);

            scene.update();

            renderer.render(scene.collectDrawList());
        });

        gameWindow.open();
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}
