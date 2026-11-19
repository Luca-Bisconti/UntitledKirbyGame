package it.unicam.cs.mpgc.rpg130396.javaFX;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
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


        TextureRegion sprite = new TextureRegion(JavaFXTexture.solid(610, 64, 0xFFFF69B4));
        Transform2D transform = new Transform2D().setPosition(400, 300);

        RenderPass spritePass = (ctx, in, out) -> ctx.drawTexture(sprite, transform);

        Renderer renderer = new Renderer(
                List.of(new PassBinding(spritePass, null, gameWindow.getScreenTarget(), true)),
                gameWindow.getDrawContext());

        gameWindow.setFrameListener(dt -> {
            transform.rotate((float) dt);
            renderer.render();
        });

        gameWindow.open();
    }

    public static void launch(String[] args) {
        Application.launch(JavaFXLauncher.class, args);
    }
}