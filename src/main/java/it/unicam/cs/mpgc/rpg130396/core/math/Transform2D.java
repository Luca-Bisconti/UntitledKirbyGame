package it.unicam.cs.mpgc.rpg130396.core.math;

public class Transform2D {

    public float posX, posY;
    public float rotation;
    public float scale;

    public Transform2D(){

        posX = posY = 0;
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

    public Transform2D rotate(float rotation){
        this.rotation += rotation;
        return  this;
    }

    public Transform2D setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }


    public Transform2D set(Transform2D t){
        posX = t.posX;
        posY = t.posY;

        rotation = t.rotation;
        scale = t.scale;

        return this;
    }

    public Transform2D copy() { return new Transform2D().set(this); }

    public static void compose(Transform2D p, Transform2D l, Transform2D out) {
        float cos = (float) Math.cos(p.rotation);   
        float sin = (float) Math.sin(p.rotation);


        float x = l.posX * p.scale;
        float y = l.posY * p.scale;


        float rx = x * cos - y * sin;
        float ry = x * sin + y * cos;


        out.posX = p.posX + rx;
        out.posY = p.posY + ry;

        out.rotation = p.rotation + l.rotation;
        out.scale    = p.scale * l.scale;
    }

}
