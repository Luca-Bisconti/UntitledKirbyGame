package it.unicam.cs.mpgc.rpg130396.javaFX.resources;

import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.util.Objects;

public class JavaFXTexture implements Texture {

    private final Image image;

    public JavaFXTexture(Image image) {
        this.image = image;
    }



    public static JavaFXTexture solid(int w, int h, int argb) {
        WritableImage img = new WritableImage(w, h);
        PixelWriter pw = img.getPixelWriter();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                pw.setArgb(x, y, argb);
        return new JavaFXTexture(img);
    }

    public Image image() { return image; }

    @Override public int width()  { return (int) image.getWidth(); }
    @Override public int height() { return (int) image.getHeight(); }
}