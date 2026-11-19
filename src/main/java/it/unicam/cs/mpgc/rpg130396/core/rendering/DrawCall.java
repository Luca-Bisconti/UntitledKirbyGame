package it.unicam.cs.mpgc.rpg130396.core.rendering;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

public record DrawCall(TextureRegion textureRegion, Transform2D transform2D, int layer, int zIndex) {}
