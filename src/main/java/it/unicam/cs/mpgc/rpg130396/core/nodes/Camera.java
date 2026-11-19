package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.math.Rect;

public final class Camera extends Node {

      int resX, resY;   
    private float zoom = 1f;
    private float anchorX, anchorY;            

    public Camera(String name, int resX, int resY) {
        super(name);
        this.resX = resX;
        this.resY = resY;
        this.anchorX = resX / 2f;
        this.anchorY = resY / 2f;
    }

    public void setZoom(float z) {
        if (z <= 0) throw new IllegalArgumentException("zoom must be > 0");
        zoom = z;
    }

    public void setAnchor(float x, float y) { anchorX = x; anchorY = y; }


    public Rect viewRect(float margin) {
        Transform2D w = getWorld();
        return new Rect(
                w.posX - anchorX / zoom - margin,
                w.posY - anchorY / zoom - margin,
                w.posX + (resX - anchorX) / zoom + margin,
                w.posY + (resY - anchorY) / zoom + margin);
    }


    public Transform2D toScreen(Transform2D t) {
        Transform2D c = getWorld();
        Transform2D s = t.copy();
        s.posX = (t.posX - c.posX) * zoom + anchorX;
        s.posY = (t.posY - c.posY) * zoom + anchorY;
        s.scale = t.scale * zoom;
        return s;
    }
}