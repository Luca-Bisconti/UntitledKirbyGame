package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

import java.util.Objects;

public record DrawCall(TextureRegion textureRegion, Transform2D transform2D, int layer, int zIndex) {

    public DrawCall {
        Objects.requireNonNull(textureRegion);
        Objects.requireNonNull(transform2D);
    }
}
