package it.unicam.cs.mpgc.rpg130396.core.math;

public record Rect(float x, float y, float width, float height) {

    public boolean contains(float posX, float posY){
        return (x<=posX && posX<=(x+width)) && (y<=posY && posY<=(y+height));
    }

}
