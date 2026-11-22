package it.unicam.cs.mpgc.rpg130396.javaFX.resources;

import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Objects;

public class JavaFXTextureLoader implements TextureLoader {

    private final ClassLoader classLoader;

    public JavaFXTextureLoader() {
        this(JavaFXTextureLoader.class.getClassLoader());
    }

    public JavaFXTextureLoader(ClassLoader classLoader) {
        this.classLoader = Objects.requireNonNull(classLoader);
    }

    public boolean hasResource(String texturePath) {
        return resource(texturePath) != null;
    }

    @Override
    public Texture load(String texturePath) {
        URL url = resource(texturePath);
        if (url == null) {
            throw new IllegalArgumentException("Risorsa non trovata: " + texturePath);
        }

        try (var in = url.openStream()) {
            Image image = new Image(in);
            if (image.isError()) {
                throw new IllegalStateException(
                        "Immagine non valida o formato non supportato: " + texturePath,
                        image.getException());
            }
            return new JavaFXTexture(image);
        } catch (IOException e) {
            throw new UncheckedIOException("Errore leggendo " + texturePath, e);
        }
    }

    private URL resource(String texturePath) {
        if (texturePath.startsWith("/")) {
            return classLoader.getResource(texturePath.substring(1));
        }
        return JavaFXTextureLoader.class.getResource(texturePath);
    }

    @Override
    public void unload(String TextureID) {
        throw new UnsupportedOperationException();
    }
}
