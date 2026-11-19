package it.unicam.cs.mpgc.rpg130396.core.resources;

public class TextureRegion {

    public int posX, posY;
    public int height, width;
    private final Texture texture;
    public int originX, originY;

    public TextureRegion( Texture texture){

        this.texture = texture;
        this.posX = this.posY = 0;
        this.width = texture.width();
        this.height = texture.height();
    }


    public TextureRegion setPosition(int x, int y){

        posX = x;
        posY = y;
        return this;
    }


    public TextureRegion setRegionSize(int w, int h){

        width = w;
        height = h;

        return this;
    }

    public Texture getTexture() {
        return texture;
    }
}
