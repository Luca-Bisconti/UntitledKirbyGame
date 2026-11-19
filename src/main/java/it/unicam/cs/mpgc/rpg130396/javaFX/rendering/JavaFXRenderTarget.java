package it.unicam.cs.mpgc.rpg130396.javaFX.rendering;

import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.javaFX.resources.JavaFXTexture;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class JavaFXRenderTarget implements RenderTarget {

    private final Canvas canvas;


    public JavaFXRenderTarget(Canvas canvas) {
        this.canvas = canvas;
    }


    public JavaFXRenderTarget(int width, int height) {
        this(new Canvas(width, height));
    }

    public Canvas canvas() { return canvas; }

    @Override public int width()  { return (int) canvas.getWidth(); }
    @Override public int height() { return (int) canvas.getHeight(); }


    @Override
    public Texture asTexture() {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        WritableImage img = canvas.snapshot(params, null);
        return new JavaFXTexture(img);
    }
}