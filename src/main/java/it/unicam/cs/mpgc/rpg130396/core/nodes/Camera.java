package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.math.Rect;

public final class Camera extends Node {

    private final int resX, resY;
    private float zoom = 1f;
    private float anchorX, anchorY;            

    public Camera(String name, int resX, int resY) {
        super(name);
        if (resX <= 0 || resY <= 0) {
            throw new IllegalArgumentException("Camera dimensions must be positive");
        }
        this.resX = resX;
        this.resY = resY;
        this.anchorX = resX / 2f;
        this.anchorY = resY / 2f;
    }

    public void setZoom(float z) {
        if (!Float.isFinite(z) || z <= 0) {
            throw new IllegalArgumentException("Zoom must be finite and positive");
        }
        zoom = z;
    }

    public void setAnchor(float x, float y) {
        if (!Float.isFinite(x) || !Float.isFinite(y)) {
            throw new IllegalArgumentException("Camera anchor must be finite");
        }
        anchorX = x;
        anchorY = y;
    }

    public Rect viewRect(float margin) {
        if (!Float.isFinite(margin)) {
            throw new IllegalArgumentException("Camera margin must be finite");
        }
        Transform2D world = getWorld();

        float minX = world.posX() - anchorX / zoom - margin;
        float minY = world.posY() - anchorY / zoom - margin;
        float maxX = world.posX() + (resX - anchorX) / zoom + margin;
        float maxY = world.posY() + (resY - anchorY) / zoom + margin;

        float width = maxX - minX;
        float height = maxY - minY;
        if (!Float.isFinite(width) || !Float.isFinite(height) || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Camera view must have finite, positive dimensions");
        }
        return new Rect(minX, minY, width, height);
    }


    public Transform2D toScreen(Transform2D transform) {
        Transform2D camera = getWorld();
        return new Transform2D(
                (transform.posX() - camera.posX()) * zoom + anchorX,
                (transform.posY() - camera.posY()) * zoom + anchorY,
                transform.rotation(),
                transform.scale() * zoom
        );
    }
}
