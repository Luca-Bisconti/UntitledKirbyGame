package it.unicam.cs.mpgc.rpg130396.core.nodes;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.List;
import java.util.Objects;

public final class SpriteAnimator implements Updatable {

    private final Sprite sprite;
    private SpriteAnimation animation;
    private List<TextureRegion> frames;
    private double position;
    private int frameIndex;
    private boolean paused;

    public SpriteAnimator(Sprite sprite) {
        this.sprite = Objects.requireNonNull(sprite);
    }


    public void play(SpriteAnimation animation) {
        this.animation = Objects.requireNonNull(animation);
        frames = animation.frames();
        position = 0;
        frameIndex = 0;
        paused = false;
        sprite.setRegion(frames.getFirst());
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    @Override
    public void update(double dt) {
        Updatable.requireValidDeltaTime(dt);
        if (animation == null || paused || dt == 0) {
            return;
        }

        int frameCount = frames.size();
        double frameDuration = animation.frameDuration();
        double elapsedFrames = dt / frameDuration;
        if (animation.loop()) {

            if (elapsedFrames >= 0x1.0p52) {
                elapsedFrames = (dt % (frameDuration * frameCount)) / frameDuration;
            }
            position = (position + elapsedFrames % frameCount) % frameCount;
        } else {
            position = Math.min(position + elapsedFrames, frameCount - 1);
        }

        int nextFrame = (int) position;
        if (nextFrame != frameIndex) {
            frameIndex = nextFrame;
            sprite.setRegion(frames.get(frameIndex));
        }
    }
}
