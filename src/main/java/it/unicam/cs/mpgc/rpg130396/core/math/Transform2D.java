package it.unicam.cs.mpgc.rpg130396.core.math;

public record Transform2D(float posX, float posY, float rotation, float scale) {

    public Transform2D {
        if (!Float.isFinite(posX) || !Float.isFinite(posY) || !Float.isFinite(rotation)) {
            throw new IllegalArgumentException("Position and rotation must be finite");
        }
        if (!Float.isFinite(scale) || scale <= 0) {
            throw new IllegalArgumentException("Scale must be finite and positive");
        }
    }

    public Transform2D() {
        this(0, 0, 0, 1);
    }

    public Transform2D withPosition(float x, float y) {
        return new Transform2D(x, y, rotation, scale);
    }

    public Transform2D translate(float dx, float dy) {
        return withPosition(posX + dx, posY + dy);
    }

    public Transform2D withRotation(float radians) {
        return new Transform2D(posX, posY, radians, scale);
    }

    public Transform2D rotate(float radians) {
        return withRotation(rotation + radians);
    }

    public Transform2D withScale(float factor) {
        return new Transform2D(posX, posY, rotation, factor);
    }

    public static Transform2D compose(Transform2D parent, Transform2D local) {
        float cos = (float) Math.cos(parent.rotation);
        float sin = (float) Math.sin(parent.rotation);
        float x = local.posX * parent.scale;
        float y = local.posY * parent.scale;
        float rotatedX = x * cos - y * sin;
        float rotatedY = x * sin + y * cos;
        return new Transform2D(
                parent.posX + rotatedX,
                parent.posY + rotatedY,
                parent.rotation + local.rotation,
                parent.scale * local.scale
        );
    }
}
