package it.unicam.cs.mpgc.rpg130396.core.math;

public class Transform2D {

    public float posX, posY;
    public  float scaleX, scaleY;
    public float originX, originY;
    public float rotation;

    public Transform2D(){

        posX = posY = 0;
        originX = originY = 0.5f;
        scaleX = scaleY = 1.0f;
        rotation = 0;
    }

    public Transform2D setPosition(float x, float y) {
        this.posX = x;
        this.posY = y;
        return this;
    }

    public Transform2D translate(float dx, float dy) {
        this.posX += dx;
        this.posY += dy;
        return this;
    }

    public Transform2D setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        return this;
    }

    public Transform2D rotate(float rotation){
        this.rotation += rotation;
        return  this;
    }

    public Transform2D setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    public Transform2D setOrigin(float originX, float originY) {
        this.originX = originX;
        this.originY = originY;
        return this;
    }
}
