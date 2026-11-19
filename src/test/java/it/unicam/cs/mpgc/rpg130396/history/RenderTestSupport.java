package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;

final class RenderTestSupport {
    private RenderTestSupport() {}

    record Target(int width, int height) implements RenderTarget {
        public Texture asTexture() {
            return new Texture() {
                public int width() { return width; }
                public int height() { return height; }
            };
        }
    }

    static final class Context implements DrawContext {
        public void clear() {}
        public void setTarget(RenderTarget target) {}
        public void drawTexture(TextureRegion region, Transform2D transform) {}
    }
}
