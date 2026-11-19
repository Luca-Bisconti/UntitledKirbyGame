package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.math.DirectionalLight;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectionalLightTest {
    @Test
    void normalizesDirectionAndRejectsInvalidValues() {
        var light = new DirectionalLight(3, 4, 0.75f);
        assertEquals(0.6f, light.directionX(), 0.0001f);
        assertEquals(0.8f, light.directionY(), 0.0001f);
        assertEquals(0.75f, light.intensity());
        assertThrows(IllegalArgumentException.class, () -> light.setDirection(0, 0));
        assertThrows(IllegalArgumentException.class, () -> light.setIntensity(-1));
    }
}
