package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.math.Transform2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Transform2DTest {
    @Test
    void combinesIncrementalTranslations() {
        var transform = new Transform2D().setPosition(2, 3).translate(-5, 7);
        assertEquals(-3f, transform.posX);
        assertEquals(10f, transform.posY);
    }
}
