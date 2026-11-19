
package it.unicam.cs.mpgc.rpg130396.core.math;

public class DirectionalLight {

    private float directionX;
    private float directionY;
    private float intensity;

    public DirectionalLight(
            float directionX,
            float directionY,
            float intensity
    ) {
        setDirection(directionX, directionY);
        setIntensity(intensity);
    }

    public void setDirection(float x, float y) {

        float length = (float) Math.sqrt(x * x + y * y);

        if (length == 0) {
            throw new IllegalArgumentException(
                    "Light direction cannot be zero"
            );
        }

        directionX = x / length;
        directionY = y / length;
    }

    public void setIntensity(float intensity) {

        if (intensity < 0) {
            throw new IllegalArgumentException(
                    "Light intensity cannot be negative"
            );
        }

        this.intensity = intensity;
    }

    public float directionX() {
        return directionX;
    }

    public float directionY() {
        return directionY;
    }

    public float intensity() {
        return intensity;
    }
}
