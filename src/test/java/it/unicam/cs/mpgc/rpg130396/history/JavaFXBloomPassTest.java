package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXBloomPass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaFXBloomPassTest {
    @Test
    void rejectsARadiusOutsideTheFilterRange() {
        assertDoesNotThrow(() -> new JavaFXBloomPass(0.5, 0, 1, true));
        assertThrows(IllegalArgumentException.class,
                () -> new JavaFXBloomPass(0.5, JavaFXBloomPass.MAX_RADIUS + 1, 1, true));
    }
}
