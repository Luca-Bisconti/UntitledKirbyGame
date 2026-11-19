package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.DrawContext;
import it.unicam.cs.mpgc.rpg130396.core.rendering.RenderTarget;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompositorTest {
    @Test
    void scalesAnEntireLayerToTheOutputWidth() {
        var transforms = new java.util.ArrayList<Transform2D>();
        var texture = new Texture() {
            public int width() { return 80; }
            public int height() { return 45; }
        };
        var input = new RenderTarget() {
            public int width() { return 80; }
            public int height() { return 45; }
            public Texture asTexture() { return texture; }
        };
        var context = new DrawContext() {
            public void clear() {}
            public void setTarget(RenderTarget target) {}
            public void drawTexture(TextureRegion region, Transform2D transform) {
                transforms.add(transform);
            }
        };
        new Compositor().render(context, input, new RenderTestSupport.Target(160, 90));
        assertEquals(1, transforms.size());
        assertEquals(2f, transforms.getFirst().scale());
    }
}
