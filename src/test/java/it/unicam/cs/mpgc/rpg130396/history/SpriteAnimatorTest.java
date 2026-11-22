package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Sprite;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAnimation;
import it.unicam.cs.mpgc.rpg130396.core.nodes.SpriteAnimator;
import it.unicam.cs.mpgc.rpg130396.core.resources.SpriteAtlas;
import it.unicam.cs.mpgc.rpg130396.core.resources.Texture;
import it.unicam.cs.mpgc.rpg130396.core.resources.TextureRegion;
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

    @Test
    void changesFacingWithoutMovingTheAtlasRegion() {
        var texture = new Texture() {
            public int width() { return 16; }
            public int height() { return 8; }
        };
        var frame = new TextureRegion(texture, 8, 0, 8, 8).setOrigin(3, 7);
        var sprite = new Sprite("player");
        var animator = new SpriteAnimator(sprite);
        animator.play(new SpriteAnimation(java.util.List.of(frame), 0.25, true));
        animator.setFlippedHorizontally(true);
        assertTrue(sprite.getRegion().isFlippedHorizontally());
        assertEquals(8, sprite.getRegion().posX());
        assertEquals(3, sprite.getRegion().originX());
        assertFalse(frame.isFlippedHorizontally());
    }
}
