package it.unicam.cs.mpgc.rpg130396.core.resources;

import java.util.List;
import java.util.Objects;

public final class SpriteAnimation {

    private final List<TextureRegion> frames;
    private final double frameDuration;
    private final boolean loop;

    public SpriteAnimation(List<TextureRegion> frames, double frameDuration, boolean loop) {
        Objects.requireNonNull(frames);
        if (frames.isEmpty()) {
            throw new IllegalArgumentException("An animation must have at least one frame");
        }
        if (!Double.isFinite(frameDuration) || frameDuration <= 0) {
            throw new IllegalArgumentException("Frame duration must be finite and positive");
        }
        this.frames = copyFrames(frames);
        this.frameDuration = frameDuration;
        this.loop = loop;
    }

    public List<TextureRegion> frames() {
        return copyFrames(frames);
    }

    public double frameDuration() {
        return frameDuration;
    }

    public boolean loop() {
        return loop;
    }

    private static List<TextureRegion> copyFrames(List<TextureRegion> frames) {
        return frames.stream()
                .map(region -> new TextureRegion(region.getTexture(),
                        region.posX(), region.posY(), region.width(), region.height())
                        .setOrigin(region.originX(), region.originY())
                        .setFlippedHorizontally(region.isFlippedHorizontally()))
                .toList();
    }
}
