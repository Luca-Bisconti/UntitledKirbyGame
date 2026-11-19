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
    private final SnapshotParameters snapshotParameters = new SnapshotParameters();
    private WritableImage snapshot;
    private JavaFXTexture snapshotTexture;


    public JavaFXRenderTarget(Canvas canvas) {
        this.canvas = canvas;
        snapshotParameters.setFill(Color.TRANSPARENT);
    }


    public JavaFXRenderTarget(int width, int height) {
        this(new Canvas(width, height));
    }

    public Canvas canvas() { return canvas; }

    @Override public int width()  { return (int) canvas.getWidth(); }
    @Override public int height() { return (int) canvas.getHeight(); }





    @Override
    public Texture asTexture() {
        int width = Math.max(1, (int) Math.ceil(canvas.getWidth()));
        int height = Math.max(1, (int) Math.ceil(canvas.getHeight()));
        if (snapshot == null || snapshot.getWidth() != width || snapshot.getHeight() != height) {
            snapshot = new WritableImage(width, height);
            snapshotTexture = new JavaFXTexture(snapshot);
        }
        canvas.snapshot(snapshotParameters, snapshot);
        return snapshotTexture;
    }
}
