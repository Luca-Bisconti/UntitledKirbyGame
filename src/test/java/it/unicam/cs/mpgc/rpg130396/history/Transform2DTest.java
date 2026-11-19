package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Transform2DTest {
    @Test
    void combinesIncrementalTranslations() {
        var transform = new Transform2D().withPosition(2, 3).translate(-5, 7);
        assertEquals(-3f, transform.posX());
        assertEquals(10f, transform.posY());
    }

    @Test
    void appliesParentScaleAndRotationToChildTranslation() {
        var parent = new Transform2D(10, 20, (float) (Math.PI / 2), 2);
        var child = new Transform2D(3, 0, 0, 0.5f);
        var world = Transform2D.compose(parent, child);
        assertEquals(10f, world.posX(), 0.0001f);
        assertEquals(26f, world.posY(), 0.0001f);
        assertEquals(1f, world.scale());
        assertEquals(3f, child.posX());
    }
}
