package it.unicam.cs.mpgc.rpg130396.core.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;


public class TextureStore {

    private final TextureLoader textureLoader;
    private final Map<String, Texture> loadedTextures = new HashMap<>();

    public TextureStore(TextureLoader textureLoader) {
        this.textureLoader = Objects.requireNonNull(textureLoader);
    }

    public void load(String filePath) {
        loadedTextures.computeIfAbsent(Objects.requireNonNull(filePath), path -> {
            Texture texture = textureLoader.load(path);
            if (texture == null) {
                throw new IllegalStateException("Texture loader returned null for " + path);
            }
            return texture;
        });
    }


    public void remove(String key) {
        if (loadedTextures.remove(Objects.requireNonNull(key)) == null) {
            throw new NoSuchElementException("Texture not loaded: " + key);
        }
    }

    public void clear() {
        loadedTextures.clear();
    }


    public Texture get(String key) {
        return loadedTextures.get(Objects.requireNonNull(key));
    }
}
