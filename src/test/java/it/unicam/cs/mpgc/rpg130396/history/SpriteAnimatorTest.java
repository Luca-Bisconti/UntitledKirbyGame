package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpriteAnimatorTest {
    @Test
    void advancesThroughAtlasFramesAndLoops() {
        var texture = new Texture() {
            public int width() { return 16; }
            public int height() { return 8; }
        };
        var atlas = new SpriteAtlas(texture, 8, 8, 4, 4);
        var sprite = new Sprite("animated");
        var animator = new SpriteAnimator(sprite);
        animator.play(new SpriteAnimation(java.util.List.of(atlas.frame(0), atlas.frame(1)), 0.125, true));
        assertEquals(0, sprite.getRegion().posX());
        animator.update(0.125);
        assertEquals(8, sprite.getRegion().posX());
        animator.update(0.125);
        assertEquals(0, sprite.getRegion().posX());
    }
}
