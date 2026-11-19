package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.rendering.Compositor;
import it.unicam.cs.mpgc.rpg130396.core.rendering.PassBinding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PassBindingTest {
    @Test
    void cannotReadAndWriteTheSameTargetInAPass() {
        var target = new RenderTestSupport.Target(16, 9);
        assertThrows(IllegalArgumentException.class, () -> new PassBinding(new Compositor(), target, target, false));
    }
}
