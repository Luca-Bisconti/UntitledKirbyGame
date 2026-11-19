package it.unicam.cs.mpgc.rpg130396.javaFX.resources;

import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureLoader;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public class JavaFXTextureLoader implements TextureLoader {


    @Override
    public Texture load(String texturePath) {

        try (var in = Objects.requireNonNull(
                JavaFXTexture.class.getResourceAsStream(texturePath),
                "Risorsa non trovata: " + texturePath)) {

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

    @Override
    public void unload(String TextureID) {
        throw new UnsupportedOperationException();
    }
}
