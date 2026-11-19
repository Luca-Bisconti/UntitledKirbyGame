package it.unicam.cs.mpgc.rpg130396.core.resources;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TextureStore{

    private TextureLoader textureLoader;
    private ConcurrentHashMap<String,Texture> loadedTextures;


    public TextureStore(TextureLoader textureLoader){

        this.textureLoader = Objects.requireNonNull(textureLoader);
        loadedTextures = new ConcurrentHashMap<>(32,0.75f);

    }

    public void load(String filePath){
        Objects.requireNonNull(filePath);

        textureLoader.load(filePath);
    }

    public void remove(String key){

        Objects.requireNonNull(key);

        if(!loadedTextures.containsKey(key))
            throw new NoSuchElementException();

        loadedTextures.remove(key);
    }

    public void clear(){
        loadedTextures.clear();
    }

    public Texture get(String key){
        return null;
    }

}
