package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.math.Rect;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


public final class CameraFollowComponent implements PostUpdatable {

    private final Camera camera;
    private final Node target;
    private final float offsetX;
    private final float levelMinX;
    private final float levelMaxX;
    private final Set<ParallaxComponent> backgrounds = new LinkedHashSet<>();


    public CameraFollowComponent(Camera camera, Node target, float offsetX, float levelMinX, float levelMaxX) {
        this.camera = Objects.requireNonNull(camera);
        this.target = Objects.requireNonNull(target);
        if (!Float.isFinite(offsetX) || !Float.isFinite(levelMinX)
                || !Float.isFinite(levelMaxX) || levelMinX >= levelMaxX) {
            throw new IllegalArgumentException("Expected a finite follow offset and ordered, finite level bounds");
        }
        this.offsetX = offsetX;
        this.levelMinX = levelMinX;
        this.levelMaxX = levelMaxX;
    }


    public void addParallax(ParallaxComponent parallax) {
        if (!Objects.requireNonNull(parallax).usesCamera(camera)) {
            throw new IllegalArgumentException("Parallax must use the followed camera");
        }
        backgrounds.add(parallax);
    }

    @Override
    public void postUpdate(double dt) {
        Updatable.requireValidDeltaTime(dt);
        Rect view = camera.viewRect(0);
        double anchorOffset = (double) camera.getWorld().posX() - view.x();
        double minViewLeft = levelMinX;
        double maxViewLeft = (double) levelMaxX - view.width();
        double desiredViewLeft = (double) target.getWorld().posX() + offsetX - anchorOffset;

        double viewLeft = maxViewLeft < minViewLeft
                ? (minViewLeft + maxViewLeft) / 2
                : Math.clamp(desiredViewLeft, minViewLeft, maxViewLeft);
        camera.setWorldX((float) (viewLeft + anchorOffset));
        for (ParallaxComponent background : backgrounds) {
            background.update(dt);
        }
    }
}
