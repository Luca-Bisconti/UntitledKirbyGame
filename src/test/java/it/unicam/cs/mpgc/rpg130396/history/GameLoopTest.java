package it.unicam.cs.mpgc.rpg130396.history;

import it.unicam.cs.mpgc.rpg130396.core.nodes.Camera;
import it.unicam.cs.mpgc.rpg130396.core.engine.GameLoop;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Node;
import it.unicam.cs.mpgc.rpg130396.core.input.PolledInput;
import it.unicam.cs.mpgc.rpg130396.core.rendering.Renderer;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Scene;
import it.unicam.cs.mpgc.rpg130396.core.nodes.Updatable;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameLoopTest {
    @Test
    void discardsInitialTimestampAndCapsLongFrames() {
        var deltas = new java.util.ArrayList<Double>();
        var root = new Node("root") {};
        root.addComponent((Updatable) deltas::add);
        var target = new RenderTestSupport.Target(16, 9);
        var renderer = new Renderer(new RenderTestSupport.Context(), target, java.util.List.of());
        var loop = new GameLoop(renderer, new Scene(root, new Camera("camera", 16, 9), true), new PolledInput());
        loop.update(1_000_000_000L);
        assertTrue(deltas.isEmpty());
        loop.update(2_000_000_000L);
        assertEquals(java.util.List.of(0.05), deltas);
    }
}
