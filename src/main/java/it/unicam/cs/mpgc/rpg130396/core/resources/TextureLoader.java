package it.unicam.cs.mpgc.rpg130396.core.resources;

public interface TextureLoader{


    public Texture load(String fileName);
    public void unload(String TextureID);

}
