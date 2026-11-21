package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.javaFX.rendering.JavaFXNativeBloomPass;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JavaFXNativeBloomPassTest {
    @Test
    void validatesTheThresholdAndRequiresJavaFXRenderTargets() {
        assertThrows(IllegalArgumentException.class, () -> new JavaFXNativeBloomPass(-0.01));
        var pass = new JavaFXNativeBloomPass(0.5);
        var input = new RenderTestSupport.Target(16, 9);
        var output = new RenderTestSupport.Target(16, 9);
        assertThrows(IllegalArgumentException.class, () -> pass.render(new RenderTestSupport.Context(), input, output));
    }
}
