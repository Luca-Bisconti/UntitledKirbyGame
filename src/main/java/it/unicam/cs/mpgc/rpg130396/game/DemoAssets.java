package it.unicam.cs.mpgc.rpg130396.game;

import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;

import java.util.List;
import java.util.Objects;


public record DemoAssets(List<Background> backgrounds, Texture bridge,
                         SpriteAnimation idle, SpriteAnimation walk, SpriteAnimation crouch) {

    public DemoAssets {
        backgrounds = List.copyOf(backgrounds);
        Objects.requireNonNull(bridge);
        Objects.requireNonNull(idle);
        Objects.requireNonNull(walk);
        if (crouch == null) {
            crouch = idle;
        }
    }


    public record Background(String name, Texture texture, float centerX, float centerY,
                             float factor, boolean coversViewport) {
        public Background {
            Objects.requireNonNull(name);
            Objects.requireNonNull(texture);
            if (!Float.isFinite(centerX) || !Float.isFinite(centerY) || !Float.isFinite(factor)) {
                throw new IllegalArgumentException("Background position and parallax must be finite: " + name);
            }
        }
    }
}
