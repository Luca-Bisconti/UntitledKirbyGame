package it.unicam.cs.mpgc.rpg130396.core.nodes;

import java.util.Objects;


public final class ParallaxComponent implements Updatable {

    private final Node background;
    private final Camera camera;
    private final float factor;
    private final float initialBackgroundX;
    private final float initialCameraX;


    public ParallaxComponent(Node background, Camera camera, float factor) {
        this.background = Objects.requireNonNull(background);
        this.camera = Objects.requireNonNull(camera);
        if (!Float.isFinite(factor)) {
            throw new IllegalArgumentException("Parallax factor must be finite");
        }
        this.factor = factor;
        initialBackgroundX = background.getWorld().posX();
        initialCameraX = camera.getWorld().posX();
    }

    @Override
    public void update(double dt) {
        Updatable.requireValidDeltaTime(dt);
        double cameraDisplacement = (double) camera.getWorld().posX() - initialCameraX;
        background.setWorldX((float) (initialBackgroundX + (1.0 - factor) * cameraDisplacement));
    }
}
