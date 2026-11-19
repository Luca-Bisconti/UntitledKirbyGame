package it.unicam.cs.mpgc.rpg130396.javaFX.window;

import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.window.FrameListener;
import it.unicam.cs.mpgc.rpg130396.core.window.Window;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXDrawContext;
import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXRenderTarget;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class JavaFXWindow implements Window {

    private final Stage stage;
    private final Canvas canvas = new Canvas(800, 600);
    private final JavaFXRenderTarget screen = new JavaFXRenderTarget(canvas);
    private final JavaFXDrawContext drawContext = new JavaFXDrawContext(screen);

    private FrameListener listener;
    private AnimationTimer loop;
    private String iconPath;

    public JavaFXWindow(Stage stage, String title) {
        this.stage = stage;
        stage.setTitle(title);
        stage.setScene(new Scene(new Group(canvas)));
    }

    @Override
    public void open() {
        stage.show();
        loop = new AnimationTimer() {
            private long last = 0;
            @Override public void handle(long now) {
                if (last != 0 && listener != null)
                    listener.onFrame((now - last) / 1_000_000_000.0);
                last = now;
            }
        };
        loop.start();
    }

    @Override
    public void close() {
        if (loop != null) loop.stop();
        stage.close();
    }


    @Override public Window setTitle(String title) { stage.setTitle(title); return this; }

    @Override public Window setWidth(double width) {
        canvas.setWidth(width);
        stage.sizeToScene();
        return this;
    }

    @Override public Window setHeight(double height) {
        canvas.setHeight(height);
        stage.sizeToScene();
        return this;
    }

    @Override public Window setResizable(boolean r) { stage.setResizable(r); return this; }

    @Override public Window setIconFilePath(String path) {
        this.iconPath = path;
        stage.getIcons().setAll(new Image(path));
        return this;
    }

    @Override public Window setFrameListener(FrameListener l) { this.listener = l; return this; }


    @Override public String getTitle()        { return stage.getTitle(); }
    @Override public double getWidth()        { return canvas.getWidth(); }
    @Override public double getHeight()       { return canvas.getHeight(); }
    @Override public boolean isResizable()    { return stage.isResizable(); }
    @Override public String getIconFilePath() { return iconPath; }

    @Override public DrawContext getDrawContext()   { return drawContext; }
    @Override public RenderTarget getScreenTarget() { return screen; }
}