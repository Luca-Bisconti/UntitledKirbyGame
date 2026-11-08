package it.unicam.cs.mpgc.rpg130396.core.resources;

public class TextureRegion {

    int posX, posY;
    int height, width;

    public TextureRegion(){

        posX = posY = 0;
        height = width = 1;
    }


    public TextureRegion setPosition(int x, int y){

        posX = x;
        posY = y;


        return this;
    }


    public TextureRegion setRegionSize(int h, int w){

        height = h;
        width = w;

        return this;
    }
}
